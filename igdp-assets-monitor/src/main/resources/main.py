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
from connect_database import connect_database
from Test_Login_Platform import Test_Login_Platform
from apscheduler.schedulers.blocking import BlockingScheduler
# if __name__ == "__main__":
#     logging.basicConfig(filename='task_test.log', filemode='a', level=logging.INFO,format='%(asctime)s - %(levelname)s - %(message)s', encoding='utf-8')
#     #database config
#     logging.info("Script started successfully.")
#     config=configparser.ConfigParser()
#     config.read('./setting.ini')
#     username = config['Database']['User']
#     raw_password = config['Database']['Password']
#     host = config['Database']['Host']
#     port = int(config['Database']['Port'])
#     database = config['Database']['DatabaseName']
#     tableName_tested=config['table']['tableName_tested']
#     web_inspection_info=config['table']["web_inspection_info"]
#     web_inspection_submenu=config['table']["web_inspection_submenu"]
#     web_auto_task_summary=config['table']["web_auto_task_summary"]
#     print(host+""+username+""+raw_password+" port "+str(port)+" database "+database)
#     db = connect_database(host,username,raw_password, port, database)
#     task_info=db.Gain_task_info(web_inspection_info)    
#     for task in task_info:
#         element_need_test=db.Gain_ALL_Data(tableName_tested,task['taskid'])
#         Test_User_Case=Test_Login_Platform(task['system_address'],task['driver_select'],task)
#         Login_info=db.find_Login_button(task['taskid'],tableName_tested)[0]
#         Test_User_Case.Test_Login(Login_info)
#         if Test_User_Case.Report["Login"]["state"]==True:
#             for rs in element_need_test:
#                 if rs['inspection_element_name']=='Login':
#                     continue
#                 # 使用submenu 数据库
#                 # if rs["Exist_Parent_Menu_Number"]!=None:
#                 #     print(rs["Inspection_Element_Name"])
#                 #     Parent_Menu=db.Gain_parent_menu_info(web_inspection_submenu,rs["Exist_Parent_Menu_Number"])
#                 #     print(Parent_Menu)
#                 #     Test_User_Case.Test_subMenu_Button(rs['Element_Location'],rs['Inspection_Element_Name'],rs["Inspection_Success_Element"].split(','),Parent_Menu,rs["Shot_Screen"])
#                 #     continue

#                 #使用递归检索
#                 if rs["parent_menu_id"]!=0:
#                     print(rs["inspection_element_name"])
#                     Test_User_Case.Test_button_new(rs["element_location"],rs['inspection_element_name'],rs["inspection_success_element"].split(','),rs["shot_screen"],element_need_test,rs["parent_menu_id"])
#                     continue

#                 Test_User_Case.Test_single_Button(rs['element_location'],rs['inspection_element_name'],rs["inspection_success_element"].split(','),rs['shot_screen'])
#         print(Test_User_Case.Report)
#         db.insert_task_summary(web_auto_task_summary,Test_User_Case.Report,task["taskid"])
#         Test_User_Case.Quit_test()
#         Data_Frame=pd.DataFrame(Test_User_Case.Report).T
#         Data_Frame.to_excel('./New_Output.xlsx', engine='openpyxl')
#         new_record_table="web_auto_test"
#         for operation, details in Test_User_Case.Report.items():
#             db.Insert_Data(new_record_table,operation, details['state'], details['start_time'], details['end_time'], details['Response_Time'],task['TaskID'])
#         Data_Frame.reset_index(inplace=True)
#         Data_Frame.columns = ['operation', 'state', 'start_time', 'end_time', 'response_time']
#         logging.info(f"Script executed successfully.")

#     db.close_database()


def quartz_to_unix_cron(quartz_cron):
    parts = quartz_cron.split()
    if len(parts) < 6:
        raise ValueError("Invalid Quartz cron expression")
    
    # 移除秒字段
    parts.pop(0)
    
    # 年字段移除
    if len(parts) == 6:
        parts.pop()

    day_of_month = parts[2]
    day_of_week = parts[4]
    if day_of_month == '?':
        parts[2] = '*'
    if day_of_week == '?':
        parts[4] = '*'

    if parts[4].isdigit():
        day_of_week = str((int(parts[4]) % 7))
        parts[4] = day_of_week

    return ' '.join(parts)

def run_task(task):
    logging.basicConfig(filename='task_test.log', filemode='a', level=logging.INFO,format='%(asctime)s - %(levelname)s - %(message)s', encoding='utf-8')
    #database config
    logging.info("Script started successfully.")
    config=configparser.ConfigParser()
    config.read('./setting.ini')
    username = config['Database']['User']
    raw_password = config['Database']['Password']
    host = config['Database']['Host']
    port = int(config['Database']['Port'])
    database = config['Database']['DatabaseName']
    tableName_tested=config['table']['tableName_tested']
    web_inspection_info=config['table']["web_inspection_info"]
    web_inspection_submenu=config['table']["web_inspection_submenu"]
    web_auto_task_summary=config['table']["web_auto_task_summary"]
    print(host+""+username+""+raw_password+" port "+str(port)+" database "+database)
    # db = connect_database(host,username,raw_password, port, database)
    task_info=db.Gain_task_info(web_inspection_info)    
    for task in task_info:
        element_need_test=db.Gain_ALL_Data(tableName_tested,task['taskid'])
        Test_User_Case=Test_Login_Platform(task['system_address'],task['driver_select'],task)
        Login_info=db.find_Login_button(task['taskid'],tableName_tested)[0]
        Test_User_Case.Test_Login(Login_info)
        if Test_User_Case.Report["Login"]["state"]==True:
            for rs in element_need_test:
                if rs['inspection_element_name']=='Login':
                    continue
                # 使用submenu 数据库
                # if rs["Exist_Parent_Menu_Number"]!=None:
                #     print(rs["Inspection_Element_Name"])
                #     Parent_Menu=db.Gain_parent_menu_info(web_inspection_submenu,rs["Exist_Parent_Menu_Number"])
                #     print(Parent_Menu)
                #     Test_User_Case.Test_subMenu_Button(rs['Element_Location'],rs['Inspection_Element_Name'],rs["Inspection_Success_Element"].split(','),Parent_Menu,rs["Shot_Screen"])
                #     continue

                #使用递归检索
                if rs["parent_menu_id"]!=0:
                    print(rs["inspection_element_name"])
                    Test_User_Case.Test_button_new(rs["element_location"],rs['inspection_element_name'],rs["inspection_success_element"].split(','),rs["shot_screen"],element_need_test,rs["parent_menu_id"])
                    continue

                Test_User_Case.Test_single_Button(rs['element_location'],rs['inspection_element_name'],rs["inspection_success_element"].split(','),rs['shot_screen'])
        print(Test_User_Case.Report)
        db.insert_task_summary(web_auto_task_summary,Test_User_Case.Report,task["taskid"])
        Test_User_Case.Quit_test()
        Data_Frame=pd.DataFrame(Test_User_Case.Report).T
        Data_Frame.to_excel('./New_Output.xlsx', engine='openpyxl')
        new_record_table="web_auto_test"
        for operation, details in Test_User_Case.Report.items():
            db.Insert_Data(new_record_table,operation, details['state'], details['start_time'], details['end_time'], details['response_time'],task['taskid'])
        Data_Frame.reset_index(inplace=True)
        Data_Frame.columns = ['operation', 'state', 'start_time', 'end_time', 'response_time']
        logging.info(f"Script executed successfully.")

    # db.close_database()

def refresh_jobs():
    scheduler.remove_all_jobs()
    task_info = db.Gain_task_info(config['table']["web_inspection_info"])
    for task in task_info:
        quartz_cron = task['test_frequency']
        minute, hour, day, month, day_of_week = quartz_to_unix_cron(quartz_cron).split()
        scheduler.add_job(
            run_task,
            'cron',
            minute=minute,
            hour=hour,
            day=day,
            month=month,
            day_of_week=day_of_week,
            args=[task],
            max_instances=3
        )

def main():
    global db, scheduler,config  
    config = configparser.ConfigParser()
    config.read('./setting.ini')
    db = connect_database(config['Database']['Host'], config['Database']['User'],
                          config['Database']['Password'], int(config['Database']['Port']),
                          config['Database']['DatabaseName'])

    scheduler = BlockingScheduler()
    refresh_jobs()
    # 每隔一定时间刷新任务配置
    scheduler.add_job(refresh_jobs, 'interval', minutes=10)

    try:
        scheduler.start()
    except (KeyboardInterrupt, SystemExit):
        scheduler.shutdown()
    finally:
        db.close_database()

if __name__ == "__main__":
    main()
