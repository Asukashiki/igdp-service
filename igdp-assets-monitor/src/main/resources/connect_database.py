from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC
import time
from urllib.parse import quote_plus
import pandas as pd
import configparser
import logging
from DetectCaptcha import detect_Captcha
import psycopg2
from psycopg2.extras import DictCursor
from psycopg2 import OperationalError
class connect_database:
    # init
    def __init__(self, host, username, raw_password, port, databaseName):
        self.host = host
        self.port = port
        self.username = username
        self.password = raw_password  
        self.database = databaseName
        try:
            self.conn = psycopg2.connect(
                host=self.host,
                port=self.port,
                user=self.username,
                password=self.password,
                dbname=self.database,
                options="-c search_path=igdp_v3,public",
                cursor_factory=DictCursor,
                connect_timeout=30
            )
            self.cursor = self.conn.cursor()
            print("Connection successful")
        except OperationalError as e:
            print(f"Connection failed: {e}")

    def Gain_ALL_Data(self, tableName,taskId):
        try:
            self.cursor.execute(f"SELECT * FROM {tableName} WHERE task_number={taskId};")
            results = self.cursor.fetchall()
            for row in results:
                print(row)
            return results
        except Exception as e:
            print(f"fail to gain data: {e}")
    
    # insert result after test to the database
    def Insert_Data(self,Result_tableName,operation, state, start_time, end_time, response_time,taskId):
        sql = f"INSERT INTO {Result_tableName} (operation, state, start_time, end_time, response_time, taskid) VALUES (%s, %s, %s, %s, %s, %s)"
        self.cursor.execute(sql, (operation, state, start_time, end_time, response_time,taskId))
        self.conn.commit()

    # insert need tested element's info 
    def Insert_Element_NeedTest(self,Input_tableName,elementName,elementLocation,DisplaySign): 
        ConvertElementLocation=self.convertFormat(elementLocation)
        ConvertDisplaySign=self.convertFormat(DisplaySign)
        sql=f"INSERT INTO {Input_tableName} (inspection_element_name,element_location,inspection_success_element) VALUES (%s,%s,%s)" 
        self.cursor.execute(sql,(elementName,ConvertElementLocation,ConvertDisplaySign))
        self.conn.commit()
    
    # gain the data from the application info
    def Gain_task_info(self,inspection_table):
        tableName=inspection_table
        try:
            self.cursor.execute(f"SELECT* FROM {tableName};")
            result=self.cursor.fetchall()
            return result
        except Exception as e:
            print(f"fail to gain data: {e}")

    def find_Login_button(self,taskID,InputTable):
        tableName=InputTable
        try:
            self.cursor.execute(f"SELECT* FROM {tableName} WHERE inspection_element_name='Login' AND task_number={taskID}")
            result=self.cursor.fetchall()
            return result
        except Exception as e:
            print(f"fail to gain data: {e}")

    def Gain_parent_menu_info(self,menuTable,Exist_Parent_Menu_ID):
        sql=f"SELECT* From {menuTable} WHERE child_menu_id={Exist_Parent_Menu_ID}"
        try:
            self.cursor.execute(sql)
            result=self.cursor.fetchall()
            return result
        except Exception as e:
            print(f"fail to gain data: {e}")

    def insert_task_summary(self,Result_tableName,report,taskId):
        taskStartTime = min(report.items(), key=lambda x: x[1]['start_time'])[1]['start_time']
        taskEndTime = max(report.items(), key=lambda x: x[1]['end_time'])[1]['end_time']
        TestNumber=len(report)
        TestSuccessNumber=sum(1 for item in report.values() if item['state'] == True)
        TestFailedNumber=sum(1 for item in report.values() if item['state'] == False)
        sql=f"INSERT INTO {Result_tableName} (taskid, task_start_time, task_finish_time, task_test_total_number, task_test_success_count, task_test_failed_count) VALUES (%s, %s, %s, %s, %s, %s)"
        self.cursor.execute(sql, (taskId,taskStartTime,taskEndTime,TestNumber,TestSuccessNumber,TestFailedNumber))
        self.conn.commit()

    # convert attribute format 
    def convertFormat(self,attribute):
        NewFormat=''
        AllAttribute=attribute.split(' ')
        for att in AllAttribute:
            NewFormat=NewFormat+'.'+att
        return NewFormat
        
    def close_database(self):
        self.cursor.close()
        self.conn.close()