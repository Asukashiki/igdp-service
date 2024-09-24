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


class Test_Login_Platform:
    #Init 
    def __init__(self,test_website_url, browserDriver,taskInfo):
        self.Report=dict()
        match browserDriver:
            case 'Chrome':
                self.driver = webdriver.Chrome()
            case 'Firefox':
                self.driver = webdriver.Firefox()
            case 'Edge':
                self.driver = webdriver.Edge()
            case _:
                print("Fail,not found webdriver")
        self.driver.get(test_website_url)
        self.taskInfo=taskInfo

    #Set username and password to login
    def Test_Login(self,Login_info):
        Original_url=self.driver.current_url
        username_input = self.driver.find_element(By.CSS_SELECTOR, self.taskInfo['username_input'])
        password_input = self.driver.find_element(By.CSS_SELECTOR,self.taskInfo['password_input'])
        
        username_input.send_keys(self.taskInfo['username'])  
        password_input.send_keys(self.taskInfo['password'])

        
        
        # Login_button = self.driver.find_element(By.ID,"btnSubmit")
        print(Login_info)
        # time.sleep(5)
        Login_button=self.driver.find_element(By.CSS_SELECTOR, Login_info['element_location'])
        if self.taskInfo['captcha_input'] is not None:
            captcha_input=self.driver.find_element(By.XPATH,self.taskInfo['captcha_input'])
            while 5:
                Captcha_Character=self.Located_Captcha(self.taskInfo['captcha_image_location'])
                captcha_input.send_keys(Captcha_Character)
                Login_button.click()
                time.sleep(5)
                print(f"Now the url is: {self.driver.current_url}")
                if self.driver.current_url!=Original_url:
                    break
        else:
            Login_button.click()
            time.sleep(5)
        start_time=time.time()
        try:
            if Login_info['inspection_success_element'] is None:
                print("implicitly wait 5 seconds")
                self.driver.implicitly_wait(5)
                
            else:
                print("have target element")
                WebDriverWait(self.driver,10).until(EC.visibility_of_element_located((By.CSS_SELECTOR,Login_info['inspection_success_element'])))
                # WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.CLASS_NAME, 'apply_item_box_flexItem')))
            print("success")
            # self.Report["Login"]={"Button_Name":"Login"}
            self.Report["Login"]={"state":True}
            self.driver.save_screenshot(f'./Screen_Shot/Screen_Shot_Login.png')
            logging.info(f"Login success")
        except Exception as e:
            print("fail")
            self.Report["Login"]={"state":False}
            logging.error(f"system Login failed. Error: {e}")
        finally:
            end_time=time.time()
            self.Report["Login"]["start_time"]=self.Transform_time(start_time)
            self.Report["Login"]["end_time"]=self.Transform_time(end_time)
            response_time=end_time-start_time
            self.Report["Login"]["response_time"]=response_time
            
    #check whether page is loaded
    def Wait_Page_loaded(self):
        try:
            WebDriverWait(self.driver, 10).until(lambda driver: driver.execute_script("return document.readyState;") == "complete")
        except Exception as e: 
            print("Page loaded fail:", str(e))
            raise

    def Located_Captcha(self,image_location):
        # Captcha=self.driver.find_element(By.CSS_SELECTOR,".el-image__inner")
        Captcha=self.driver.find_element(By.XPATH,image_location)
        Captcha_base64 = Captcha.get_attribute('src')
        return detect_Captcha(Captcha_base64)

    # test single assigned button
    def Test_single_Button(self,ButtonClassOrId,ButtonName,Result_Element_Located,Shot_Screen):
        elements = self.driver.find_elements(By.CSS_SELECTOR, ButtonClassOrId)
        find_element=True
        for element in elements:
            if element.text == ButtonName:
                Test_Button=element
                find_element=False
                break
        if find_element:
            print(elements)
            print("do not find the target element")
            self.Report[ButtonName] = {
                        "state": None,
                        "start_time": None,
                        "end_time": None,
                        "response_time": None
                    }
            return
        MainPage_url=self.driver.current_url
        self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
        self.Report[ButtonName] = {
            "state": None,
            "start_time": None,
            "end_time": None,
            "response_time": None
        }
        # self.Report[ButtonName]={"Button_Name":ButtonName}
        Test_Button.click()
        start_time=time.time()
        Has_iframe, iframes = self.Check_Contain_Iframe()
        if Has_iframe:
            self.driver.switch_to.frame(iframes[0])
        try:
            self.Wait_Page_loaded()
            for i in range(len(Result_Element_Located)):
                if Result_Element_Located is not None:
                    WebDriverWait(self.driver,10).until(EC.visibility_of_element_located((By.CSS_SELECTOR,Result_Element_Located[i])))
                else:
                    self.driver.implicitly_wait(10)
            self.Report[ButtonName]["state"]=True
            if Shot_Screen==1:
                self.driver.save_screenshot(f'./Screen_Shot/Screen_Shot_ForSingleButton_{ButtonName}.png')
            logging.info(f"{ButtonName} executed successfully")
        except Exception as e:
                print("loaded fail")
                self.Report[ButtonName]["state"]=False
                logging.error(f"{ButtonName} executed failed. the error: {e}")
        finally:
            end_time=time.time()
            self.driver.switch_to.default_content()
            self.Report[ButtonName]["start_time"]=self.Transform_time(start_time)
            self.Report[ButtonName]["end_time"]=self.Transform_time(end_time)
            response_time=end_time-start_time
            self.Report[ButtonName]["response_time"]=response_time
            if Has_iframe:
                self.driver.get(MainPage_url)
                return
            
            self.driver.refresh()
            
            # if self.driver.current_url!=MainPage_url:
            #         self.driver.back()
            # else:
            #     self.driver.refresh()

            self.driver.implicitly_wait(5)
            # WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.CLASS_NAME, 'apply_item_box_flexItem')))
            self.Wait_Page_loaded()

    # using xpath to gain the location of element
    def Test_single_Button_By_Xpath(self,ButtonXpath,ButtonName,Result_Element_Located):
        elements = self.driver.find_elements(By.XPATH, ButtonXpath)
        find_element=True
        for element in elements:
            if element.text == ButtonName:
                Test_Button=element
                find_element=False
                break
        if find_element:
            print(elements)
            print("do not find the target element")
            self.Report[ButtonName] = {
                        "State": None,
                        "Start_Time": None,
                        "End_Time": None,
                        "Response_Time": None
                    }
            return
        MainPage_url=self.driver.current_url
        self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
        self.Report[ButtonName] = {
            "State": None,
            "Start_Time": None,
            "End_Time": None,
            "Response_Time": None
        }
        # self.Report[ButtonName]={"Button_Name":ButtonName}
        Test_Button.click()
        start_time=time.time()
        Has_iframe, iframes = self.Check_Contain_Iframe()
        if Has_iframe:
            self.driver.switch_to.frame(iframes[0])
        try:
            self.Wait_Page_loaded()
            for i in range(len(Result_Element_Located)):
                if Result_Element_Located!='':
                    WebDriverWait(self.driver,10).until(EC.visibility_of_element_located((By.CSS_SELECTOR,Result_Element_Located[i])))
                else:
                    self.driver.implicitly_wait(10)
            self.Report[ButtonName]["State"]=True
            self.driver.save_screenshot(f'./Screen_Shot/Screen_Shot_ForSingleButton_{ButtonName}.png')
            logging.info(f"{ButtonName} executed successfully")
        except Exception as e:
                print("loaded fail")
                self.Report[ButtonName]["State"]=False
                logging.error(f"{ButtonName} executed failed. the error: {e}")
        finally:
            end_time=time.time()
            self.driver.switch_to.default_content()
            self.Report[ButtonName]["Start_Time"]=self.Transform_time(start_time)
            self.Report[ButtonName]["End_Time"]=self.Transform_time(end_time)
            response_time=end_time-start_time
            self.Report[ButtonName]["Response_Time"]=response_time
            self.driver.get(MainPage_url)
            # if self.driver.current_url!=MainPage_url:
            #         self.driver.back()
            # else:
            #     self.driver.refresh()
            self.driver.implicitly_wait(5)
            # WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.CLASS_NAME, 'apply_item_box_flexItem')))
            self.Wait_Page_loaded()

    def click_ParentMenu(self,ButtonClassOrId,ButtonName,Result_Element_Located):
        elements = self.driver.find_elements(By.CSS_SELECTOR, ButtonClassOrId)
        find_element=True
        for element in elements:
            if element.text == ButtonName:
                Test_Button=element
                find_element=False
                break
        if find_element:
            logging.error(f"{ButtonName} parent button loaded failed.")
            return
        # self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
        Test_Button.click()
        self.driver.implicitly_wait(5)

    # check target button whether is display, can be optimized
    def check_button_has_exist(self,ButtonClassOrId,ButtonName):
        elements = self.driver.find_elements(By.CSS_SELECTOR, ButtonClassOrId)
        for element in elements:
            if element.text == ButtonName:
                Test_Button=element
                if Test_Button.is_displayed():
                    return False
                break
        return True
        
    # test the button that located in multiple menu
    def Test_subMenu_Button(self,ButtonClassOrId,ButtonName,Result_Element_Located,orderList,shot_screen):
        print("test parent menu")
        if self.check_button_has_exist(ButtonClassOrId,ButtonName):
            orderList.sort(key=lambda x:x["Click_Order"])
            for order in orderList:
                self.click_ParentMenu(order["Button_Location_Element"],order["Button_Name"],order["Display_Success_Element"])
            time.sleep(5)
        elements = self.driver.find_elements(By.CSS_SELECTOR, ButtonClassOrId)    
        find_element=True
        for element in elements:
            if element.text == ButtonName:
                Test_Button=element
                find_element=False
                break
        if find_element:
            print(elements)
            print("do not find the target element")
            self.Report[ButtonName] = {
                        "State": None,
                        "Start_Time": None,
                        "End_Time": None,
                        "Response_Time": None
                    }
            return
        MainPage_url=self.driver.current_url
        self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
        self.Report[ButtonName] = {
            "State": None,
            "Start_Time": None,
            "End_Time": None,
            "Response_Time": None
        }
        # self.Report[ButtonName]={"Button_Name":ButtonName}
        Test_Button.click()
        start_time=time.time()
        Has_iframe, iframes = self.Check_Contain_Iframe()
        if Has_iframe:
            self.driver.switch_to.frame(iframes[0])
        try:
            self.Wait_Page_loaded()
            for i in range(len(Result_Element_Located)):
                if Result_Element_Located!='':
                    WebDriverWait(self.driver,10).until(EC.visibility_of_element_located((By.CSS_SELECTOR,Result_Element_Located[i])))
                else:
                    self.driver.implicitly_wait(10)
            self.Report[ButtonName]["State"]=True
            if shot_screen==1:
                self.driver.save_screenshot(f'./Screen_Shot/Screen_Shot_ForSingleButton_{ButtonName}.png')
            logging.info(f"{ButtonName} executed successfully")
        except Exception as e:
                print("loaded fail")
                self.Report[ButtonName]["State"]=False
                logging.error(f"{ButtonName} executed failed. the error: {e}")
        finally:
            end_time=time.time()
            self.driver.switch_to.default_content()
            self.Report[ButtonName]["Start_Time"]=self.Transform_time(start_time)
            self.Report[ButtonName]["End_Time"]=self.Transform_time(end_time)
            response_time=end_time-start_time
            self.Report[ButtonName]["Response_Time"]=response_time
            if Has_iframe:
                self.driver.get(MainPage_url)
                return
            
            self.driver.refresh()
            time.sleep(2)
            
            # if self.driver.current_url!=MainPage_url:
            #         self.driver.back()
            # else:
            #     self.driver.refresh()

            self.driver.implicitly_wait(5)
            # WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.CLASS_NAME, 'apply_item_box_flexItem')))
            self.Wait_Page_loaded()

    def click_parent_byID(self,all_element,paren_menu_id):
        matched_element = next((item for item in all_element if item['id'] == paren_menu_id), None)
        print(matched_element["inspection_element_name"])
        if matched_element['parent_menu_id']!=None:
            self.click_parent_byID(all_element,matched_element['parent_menu_id'])

        elements = self.driver.find_elements(By.CSS_SELECTOR, matched_element['element_location'])
        find_element=True
        for element in elements:
            if element.text == matched_element['inspection_element_name']:
                Test_Button=element
                find_element=False
                break
        if find_element:
            logging.error(f"{matched_element['inspection_element_name']} parent button loaded failed.")
            return
        # self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
        Test_Button.click()
        self.driver.implicitly_wait(5)

    
    def Test_button_new(self,ButtonClassOrId,ButtonName,Result_Element_Located,Shot_Screen,all_element,paren_menu_id):
            if self.check_button_has_exist(ButtonClassOrId,ButtonName):
                self.click_parent_byID(all_element,paren_menu_id)
                time.sleep(1)
            
            elements = self.driver.find_elements(By.CSS_SELECTOR, ButtonClassOrId)    
            find_element=True
            for element in elements:
                if element.text == ButtonName:
                    Test_Button=element
                    find_element=False
                    break
            if find_element:
                print(elements)
                print("do not find the target element")
                self.Report[ButtonName] = {
                            "state": None,
                            "start_time": None,
                            "end_time": None,
                            "response_time": None
                        }
                return
            MainPage_url=self.driver.current_url
            self.driver.execute_script("arguments[0].scrollIntoView(true);", Test_Button)
            self.Report[ButtonName] = {
                "state": None,
                "start_time": None,
                "end_time": None,
                "response_time": None
            }
            # self.Report[ButtonName]={"Button_Name":ButtonName}
            Test_Button.click()
            start_time=time.time()
            Has_iframe, iframes = self.Check_Contain_Iframe()
            if Has_iframe:
                self.driver.switch_to.frame(iframes[0])
            try:
                self.Wait_Page_loaded()
                for i in range(len(Result_Element_Located)):
                    if Result_Element_Located is not None:
                        WebDriverWait(self.driver,10).until(EC.visibility_of_element_located((By.CSS_SELECTOR,Result_Element_Located[i])))
                    else:
                        self.driver.implicitly_wait(10)
                self.Report[ButtonName]["state"]=True
                if Shot_Screen==1:
                    self.driver.save_screenshot(f'./Screen_Shot/Screen_Shot_ForSingleButton_{ButtonName}.png')
                logging.info(f"{ButtonName} executed successfully")
            except Exception as e:
                    print("loaded fail")
                    self.Report[ButtonName]["state"]=False
                    logging.error(f"{ButtonName} executed failed. the error: {e}")
            finally:
                end_time=time.time()
                self.driver.switch_to.default_content()
                self.Report[ButtonName]["start_time"]=self.Transform_time(start_time)
                self.Report[ButtonName]["end_time"]=self.Transform_time(end_time)
                response_time=end_time-start_time
                self.Report[ButtonName]["response_time"]=response_time
                if Has_iframe:
                    self.driver.get(MainPage_url)
                    return
                
                self.driver.refresh()
                time.sleep(2)
                
                # if self.driver.current_url!=MainPage_url:
                #         self.driver.back()
                # else:
                #     self.driver.refresh()

                self.driver.implicitly_wait(5)
                # WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located((By.CLASS_NAME, 'apply_item_box_flexItem')))
                self.Wait_Page_loaded()



    #Check whether iframe exist
    def Check_Contain_Iframe(self):
        try:
            iframes = self.driver.find_elements(By.TAG_NAME, 'iframe')
            return len(iframes) > 0, iframes
        except Exception as e:
            print("Error checking iframes:", str(e))
            return False, []
        
    # Transform time to local standard time 
    def Transform_time(self,timeStamp):
        Final_Time = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(timeStamp))
        return Final_Time

    #Quit test and close website
    def Quit_test(self):
        self.driver.quit()