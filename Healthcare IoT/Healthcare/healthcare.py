import pyrebase

firebaseConfig = {
  "apiKey": "AIzaSyBFwf0H1kuLdGKXJlspQ63b4jgafjcyUDc",
  "authDomain": "healthcare-c3b0a.firebaseapp.com",
  "databaseURL": "https://healthcare-c3b0a-default-rtdb.firebaseio.com/",
  "projectId": "healthcare-c3b0a",
  "storageBucket": "healthcare-c3b0a.appspot.com",
  "messagingSenderId": "1032203371002",
  "appId": "1:1032203371002:web:0532dce790703718622443"
};

firebase = pyrebase.initialize_app(firebaseConfig)
database = firebase.database()

from time import sleep

model = "001"
conduct_check = False
complete_check = False
heart_check = -1
o2_check = -1
temp_check = -1
glucose_check = -1
heartBPM = 0
oxyLevel = 0
gluco = 0.0
temperature = 0.0

medibase =[["Healthy", 0, "Continue having a fun life :)", 0, 1, 0],
           ["Fever", 0, "Consider Aspirin or Ibuprofen Tablets for treatment.", 0, 0, 0],
           ["Flu", 0, "Consider taking Oseltamivir tablets or Flu Shots from a doctor.", 0, 0, 0],
           ["Covid-19", 0, "Isolate yourself, Drink hot water and inform medical facility.", 0, 0, 0],
           ["Common Cold", 0, "Oxymetazoline Nasal Spray with Benzonatate tablets.", 0, 0, 0],
           ["Asthma", 0, "Get an Aerosol or Ventolin Inhaler with Prednisolone tablets.", 0, 0, 0],
           ["Hyperglycemia", 0, "You require Prescribed Insulin Shots and Glucotrol tablets.", 0, 0, 0],
           ["Pre Diabetes", 0, "Consider taking Metformin Tablets.", 0, 0, 0],
           ["Hypoglycemia", 0, "You require Timely Glucagon Shots and Metformin tablets.", 0, 0, 0],
           ["Pneumonia", 0, "You require Penicillin G tablets or Augmentin tablets.", 0, 0, 0],
           ["Stress", 0, "Consider taking Aspirin to reduce pain.", 0, 1, 0],
           ["Arrhythmia", 0, "Propafenone and Acebutolol Beta Blocker tablets", 0, 0, 0],
           ["Coronary Artery Disease", 0, "Niacin supplements, Ranolazine and Acebutolol Beta Blocker tablets", 0, 0, 0],
           ["Lung Cancer", 0, "Sadly you might require chemotherapy. Cisplatin and Carboplatin can help but you need a doctor.", 0, 0, 0],
           ["Breast Cancer", 0, "Sadly Chemotherapy is needed. Adriamycin, Cytoxan and Taxotere will help from the doctor.", 0, 0, 0],
           ["Leukemia", 0, "Sadly Chemotherapy or Targeted Therapy is required. Doctors might also use immunotherapy.", 0, 0, 0]]

def inchealthyScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Healthy"):
            medibase[i][1] += 12.5

def incfeverScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Fever"):
            medibase[i][1] += 12.5

def incfluScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Flu"):
            medibase[i][1] += 12.5

def inccovidScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Covid-19"):
            medibase[i][1] += 12.5

def inccoldScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Common Cold"):
            medibase[i][1] += 12.5

def incasthmaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Asthma"):
            medibase[i][1] += 12.5

def inchyperglycemiaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Hyperglycemia"):
            medibase[i][1] += 12.5

def incprediabetesScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Pre Diabetes"):
            medibase[i][1] += 12.5

def inchypoglycemiaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Hypoglycemia"):
            medibase[i][1] += 12.5

def incpneumoniaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Pneumonia"):
            medibase[i][1] += 12.5

def incstressScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Stress"):
            medibase[i][1] += 12.5

def incarrhythmiaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Arrhythmia"):
            medibase[i][1] += 12.5

def inccoronaryScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Coronary Artery Disease"):
            medibase[i][1] += 10

def inclungcancerScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Lung Cancer"):
            medibase[i][1] += 12.5

def incbreastcancerScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Breast Cancer"):
            medibase[i][1] += 12.5

def incleukemiaScore():
    for i in range(len(medibase)):
        if (medibase[i][0] == "Leukemia"):
            medibase[i][1] += 12.5

def makePrediction(user):
    
    import re
    symptomfile = open("/home/user/Healthcare/Symptoms", "r")

    sexVal = database.child("Users").child(user).child("Sex").get()
    sex = sexVal.val()

    heightVal = database.child("Users").child(user).child("Height").get()
    height = heightVal.val()

    weightVal = database.child("Users").child(user).child("Weight").get()
    weight = weightVal.val()

    bmi = weight / pow((height/100), 2)
    bmi_rounded = round(bmi, 2)

    historyVal = database.child("Users").child(user).child("Medical History").get()
    family_history = historyVal.val()

    issuesVal = database.child("Users").child(user).child("Current Issues").get()
    current_problem = issuesVal.val()

    heartRateVal = database.child("Model").child("001").child("Heart Rate").get()
    heart_rate = heartRateVal.val()
    
    o2LevelVal = database.child("Model").child("001").child("O2").get()
    oxygen = o2LevelVal.val()
    
    bodyTempVal = database.child("Model").child("001").child("Temperature").get()
    temperature = bodyTempVal.val()
    
    bloodGlucoseVal = database.child("Model").child("001").child("Glucose").get()
    glucose = bloodGlucoseVal.val()

    fastingVal = database.child("Model").child("001").child("Fasting").get()
    fasting = fastingVal.val()

    compPrediction = database.child("Model").child("001").child("Complete Prediction").get()
    complete_prediction = compPrediction.val()

    linecount = 0
    for line in symptomfile:
        symp = []
        symp_count = 0
        linecount += 1
        for i in range(len(medibase)):
            medibase[linecount - 1][3] = linecount
            if (re.findall(medibase[i][0], line)):
                cleaned_line = line.replace(medibase[i][0] + ":", "")
                cleaned_line = cleaned_line.replace("\n", "")
                symp.append(re.split(",", cleaned_line))
                for symptom in range(len(symp[0])):
                    symp_count += 1
                medibase[i][4] = symp_count

    if(temperature >= 36.2 and temperature <= 37.5):
        inchealthyScore()
    if(oxygen >= 95):
        inchealthyScore()
    if (heart_rate >= 60 and heart_rate <= 120):
        inchealthyScore()
    if(fasting == False):
        if(glucose >= 120 and glucose <= 180):
            inchealthyScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            inchealthyScore()

    if(temperature >= 37.2):
        incfeverScore()
    if(oxygen <= 96):
        incfeverScore()
    if (heart_rate >= 90):
        incfeverScore()
    if(fasting == False):
        if(glucose >= 120 and glucose <= 180):
            incfeverScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incfeverScore()

    if(temperature > 37.2 or temperature < 36.2):
        incfluScore()
    if(oxygen < 95):
        incfluScore()
    if(sex == "M"):
        if (heart_rate > 90):
            incfluScore()
    elif (sex == "F"):
        if (heart_rate > 82):
            incfluScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
            incfluScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incfluScore()

    if(temperature > 37.2):
        inccovidScore()
    if(oxygen < 92):
        inccovidScore()
    if(sex == "M"):
        if (heart_rate > 90):
            inccovidScore()
    elif (sex == "F"):
        if (heart_rate > 82):
            inccovidScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
            inccovidScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            inccovidScore()

    if(temperature > 37.4 or temperature < 36.2):
        inccoldScore()
    if(oxygen < 97):
        inccoldScore()
    if(sex == "M"):
        if (heart_rate > 90):
            inccoldScore()
    elif (sex == "F"):
        if (heart_rate > 82):
            inccoldScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
            inccoldScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            inccoldScore()

    if(temperature > 37.2):
        incasthmaScore()
    if(oxygen < 90):
        incasthmaScore()
    if(sex == "M"):
        if (heart_rate > 100):
            incasthmaScore()
    elif (sex == "F"):
        if (heart_rate > 90):
            incasthmaScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
             incasthmaScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incasthmaScore()

    if(temperature > 37.3):
        inchyperglycemiaScore()
    if(oxygen >= 95):
        inchyperglycemiaScore()
    if (heart_rate > 88):
        inchyperglycemiaScore()
    if (fasting == False):
        if (glucose > 180):
            inchyperglycemiaScore()
    else:
        if (glucose > 120):
            inchyperglycemiaScore()

    if(temperature > 37.3):
        incprediabetesScore()
    if(oxygen >= 95):
        incprediabetesScore()
    if (heart_rate > 88):
        incprediabetesScore()
    if (fasting == False):
        if (glucose >= 150 and glucose <= 180):
            incprediabetesScore()
    else:
        if (glucose >= 110 and glucose <= 120):
            incprediabetesScore()

    if(temperature > 37.3):
        inchypoglycemiaScore()
    if(oxygen <= 94):
        inchypoglycemiaScore()
    if (heart_rate > 90):
        inchypoglycemiaScore()
    if (fasting == False):
        if (glucose <= 145):
            inchypoglycemiaScore()
    else:
        if (glucose <= 75):
            inchypoglycemiaScore()

    if(temperature > 38):
        incpneumoniaScore()
    if(oxygen <= 92):
        incpneumoniaScore()
    if (heart_rate > 110):
        incpneumoniaScore()
    if (fasting == False):
        if (glucose >= 130):
            incpneumoniaScore()
    else:
        if (glucose >= 95):
            incpneumoniaScore()

    if(temperature >= 36 and temperature <= 38):
        incstressScore()
    if(oxygen >= 95 and oxygen <= 98):
        incstressScore()
    if (heart_rate >= 100):
        incstressScore()
    if(fasting == False):
        if(glucose >= 120 and glucose <= 180):
            incstressScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incstressScore()

    if(temperature < 36.3):
        incarrhythmiaScore()
    if(oxygen <= 94):
        incarrhythmiaScore()
    if (heart_rate >= 90 or heart_rate <= 60):
        incarrhythmiaScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
            incarrhythmiaScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incarrhythmiaScore()

    if (bmi_rounded > 25) or (re.findall("Diabetes", current_problem)) or (re.findall("Diabetes", family_history)):
        inccoronaryScore()
    if(temperature >= 38 and temperature < 36.3):
        inccoronaryScore()
    if(oxygen <= 93):
        inccoronaryScore()
    if (heart_rate >= 60 and heart_rate <= 75):
        inccoronaryScore()
    if(fasting == False):
        if(glucose >= 120 and glucose <= 180):
            inccoronaryScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            inccoronaryScore()

    if(temperature > 38):
        inclungcancerScore()
    if(oxygen < 90):
        inclungcancerScore()
    if (heart_rate > 90 or heart_rate < 65):
        inclungcancerScore()
    if (fasting == False):
        if (glucose > 170):
            inclungcancerScore()
    else:
        if (glucose > 112.5):
            inclungcancerScore()

    if(sex == "F"):
        if (temperature > 38.2):
            incbreastcancerScore()
        if (oxygen < 90):
            incbreastcancerScore()
        if (heart_rate > 90):
            incbreastcancerScore()
        if (fasting == False):
            if (glucose > 160):
                incbreastcancerScore()
        else:
            if (glucose > 112.5):
                incbreastcancerScore()

    if(temperature > 38):
        incleukemiaScore()
    if(oxygen < 90):
        incleukemiaScore()
    if (heart_rate >= 72 or heart_rate <= 90):
        incleukemiaScore()
    if (fasting == False):
        if (glucose >= 120 and glucose <= 180):
            incleukemiaScore()
    else:
        if (glucose >= 80 and glucose <= 120):
            incleukemiaScore()

    high = medibase[0][1]
    prediction = [medibase[0][0]]
    symptoms_to_check = [medibase[0][4]]
    identified_diagnosis = ""
    for i in range(len(medibase)):
        if(medibase[i][1] >= high):
            if(medibase[i][1] == high):
                prediction.append(medibase[i][0])
                symptoms_to_check.append(medibase[i][4])
            else:
                prediction.clear()
                symptoms_to_check.clear()
                high = medibase[i][1]
                prediction.append(medibase[i][0])
                symptoms_to_check.append(medibase[i][4])

    if(len(prediction) == 1):
        identified_diagnosis = prediction.pop()
    else:
        problem_position = symptoms_to_check.index(max(symptoms_to_check))
        identified_diagnosis = prediction[problem_position]

    file = open("/home/user/Healthcare/Symptoms", "r")
    sympline = []
    for oneline in file:
        found = identified_diagnosis
        if(re.findall(found, oneline)):
            cleaned_line = oneline.replace(identified_diagnosis + ":", "")
            cleaned_line = cleaned_line.replace("\n", "")
            sympline.append(re.split(",", cleaned_line))

    symptom_answer = ""
    symptoms = []
    symptom_score = 0
    final_high = medibase[0][1]
    diagnosis = medibase[0][0]
    solution = medibase[0][2]

    if (sympline[0][0] == ""):
        symptomQ = "Have you been experiencing any kind of symptoms?"
        database.child("Model").child("001").child("Symptom Question").set(symptomQ)
        
        symptomSet = False
        while (symptomSet == False):
            # sleep(3)
            symptomAns = database.child("Model").child("001").child("Symptom Answer").get()
            symptomA = symptomAns.val()
            if (symptomA != ""):
                symptom_answer = symptomA
                symptomSet = True

        print(symptom_answer)
        database.child("Model").child("001").child("Symptom Question").set("")
        database.child("Model").child("001").child("Symptom Answer").set("")
        
        if ((re.findall("No", symptom_answer)) or (re.findall("not been", symptom_answer)) or (re.findall("no", symptom_answer))):
            for i in range(len(medibase)):
                if(medibase[i][4] == 1):
                    medibase[i][5] += 1
                    symptom_score = (medibase[i][5] / medibase[i][4]) * 50
                    medibase[i][1] += symptom_score
            for i in range(len(medibase)):
                if (medibase[i][1] > final_high):
                    final_high = medibase[i][1]
                    diagnosis = medibase[i][0]
                    solution = medibase[i][2]
            database.child("Users").child(user).child("Last Diagnosis").set(diagnosis)
            database.child("Users").child(user).child("Last Recommendation").set(solution)
            database.child("Model").child("001").child("Complete Prediction").set(True)
            
        elif((re.findall("Yes", symptom_answer)) or (re.findall("have been", symptom_answer)) or (re.findall("yes", symptom_answer))):
            sympline.clear()
            filecount = 0
            
            symptomQ = "OH! Can you please describe the symptoms you are having?"
            database.child("Model").child("001").child("Symptom Question").set(symptomQ)

            symptomSet = False
            while (symptomSet == False):
                # sleep(3)
                symptomAns = database.child("Model").child("001").child("Symptom Answer").get()
                symptomA = symptomAns.val()
                if (symptomA != ""):
                    symptom_answer = symptomA
                    symptomSet = True

            print(symptom_answer)
            database.child("Model").child("001").child("Symptom Question").set("")
            database.child("Model").child("001").child("Symptom Answer").set("")
            
            samefile = open("/home/user/Healthcare/Symptoms", "r")
            for line in samefile:
                filecount += 1
                for i in range(len(medibase)):
                    if (medibase[i][3] == filecount):
                        cleaned_line = line.replace(medibase[i][0] + ":", "")
                        cleaned_line = cleaned_line.replace("\n", "")
                        sympline.append(re.split(",", cleaned_line))
            for i in range(len(sympline)):
                for j in range(len(sympline[i])):
                    if (re.findall(sympline[i][j], symptom_answer) and (sympline[i][j] != "")):
                        medibase[i][5] += 1
            for i in range(len(medibase)):
                symptom_score = (medibase[i][5] / medibase[i][4]) * 50
                medibase[i][1] += symptom_score
            for i in range(len(medibase)):
                if (medibase[i][1] > final_high):
                    final_high = medibase[i][1]
                    diagnosis = medibase[i][0]
                    solution = medibase[i][2]
            database.child("Users").child(user).child("Last Diagnosis").set(diagnosis)
            database.child("Users").child(user).child("Last Recommendation").set(solution)
            database.child("Model").child("001").child("Complete Prediction").set(True)

    else:
        index = 0
        not_symptom = 0
        while (index + 1 <= len(sympline[0])):
            symptomQ = "Have you been been suffering from " + sympline[0][index]
            database.child("Model").child("001").child("Symptom Question").set(symptomQ)

            symptomSet = False
            while (symptomSet == False):
                # sleep(3)
                symptomAns = database.child("Model").child("001").child("Symptom Answer").get()
                symptomA = symptomAns.val()
                if (symptomA != ""):
                    symptom_answer = symptomA
                    symptomSet = True
            
            print(symptom_answer)
            database.child("Model").child("001").child("Symptom Question").set("")
            database.child("Model").child("001").child("Symptom Answer").set("")
            
            if ((re.findall("Yes", symptom_answer)) or (re.findall("have been", symptom_answer)) or (re.findall("yes", symptom_answer))):
                filecount = 0
                for line in symptomfile:
                    filecount += 1
                    symptoms.append(re.split(",", line))
                    for i in range(len(symptoms)):
                        for j in range(len(symptoms[i])):
                            if (re.findall(sympline[0][index], symptoms[i][j])):
                                medibase[filecount - 1][5] += 1
                for i in range(len(medibase)):
                    symptom_score = (medibase[i][5] / medibase[i][4]) * 50
                    medibase[i][1] += symptom_score
                    
            elif((re.findall("No", symptom_answer)) or (re.findall("not been", symptom_answer)) or (re.findall("no", symptom_answer))):
                not_symptom += 1
                print(not_symptom)
                if (not_symptom < 3):
                    symptomQ = "Hmm, Alright Then"
                    database.child("Model").child("001").child("Symptom Question").set(symptomQ)
                    sleep(5)
                else:
                    sympline.clear()
                    filecount = 0
                    symptomQ = "Can you then please describe the symptoms you are having?"
                    database.child("Model").child("001").child("Symptom Question").set(symptomQ)

                    symptomSet = False
                    while (symptomSet == False):
                        # sleep(3)
                        symptomAns = database.child("Model").child("001").child("Symptom Answer").get()
                        symptomA = symptomAns.val()
                        if (symptomA != ""):
                            symptom_answer = symptomA
                            symptomSet = True

                    print(symptom_answer)
                    database.child("Model").child("001").child("Symptom Question").set("")
                    database.child("Model").child("001").child("Symptom Answer").set("")
                    
                    samefile = open("/home/user/Healthcare/Symptoms", "r")
                    for line in samefile:
                        filecount += 1
                        for i in range(len(medibase)):
                            if (medibase[i][3] == filecount):
                                cleaned_line = line.replace(medibase[i][0] + ":", "")
                                cleaned_line = cleaned_line.replace("\n", "")
                                sympline.append(re.split(",", cleaned_line))
                    for i in range(len(sympline)):
                        for j in range(len(sympline[i])):
                            if (re.findall(sympline[i][j], symptom_answer) and (sympline[i][j] != "")):
                                medibase[i][5] += 1
                    for i in range(len(medibase)):
                        symptom_score = (medibase[i][5] / medibase[i][4]) * 50
                        medibase[i][1] += symptom_score

                    for i in range(len(medibase)):
                        if (medibase[i][1] > final_high):
                            final_high = medibase[i][1]
                            diagnosis = medibase[i][0]
                            solution = medibase[i][2]
                    database.child("Users").child(user).child("Last Diagnosis").set(diagnosis)
                    database.child("Users").child(user).child("Last Recommendation").set(solution)
                    database.child("Model").child("001").child("Complete Prediction").set(True)
                        
            index += 1
            
        for i in range(len(medibase)):
            if (medibase[i][1] > final_high):
                final_high = medibase[i][1]
                diagnosis = medibase[i][0]
                solution = medibase[i][2]
        database.child("Users").child(user).child("Last Diagnosis").set(diagnosis)
        database.child("Users").child(user).child("Last Recommendation").set(solution)
        database.child("Model").child("001").child("Complete Prediction").set(True)



def temp():

    import board
    import busio as io
    import RPi.GPIO as GPIO
    import adafruit_mlx90614

    GPIO.cleanup()
    i2c = io.I2C(board.SCL, board.SDA, frequency=100000)
    mlx = adafruit_mlx90614.MLX90614(i2c)

    print("Measuring Temperature Now : ")
    sleep(7)
    
    # ambientTemp = "{:.2f}".format(mlx.ambient_temperature)
    # targetTemp = "{:.2f}".format(mlx.object_temperature)

    ambientTemp = round(mlx.ambient_temperature, 2)
    targetTemp = round(mlx.object_temperature, 2)

    sleep(3)
    GPIO.cleanup()

    # print("Ambient Temperature: ", ambientTemp, " C")
    print("Target Temperature: ", targetTemp, " C")
    database.child("Model").child("001").child("Temperature").set(targetTemp)

def glucose():

    import board
    import busio
    import digitalio
    import adafruit_mcp3xxx.mcp3008 as MCP
    import RPi.GPIO as GPIO
    from adafruit_mcp3xxx.analog_in import AnalogIn

    spi = busio.SPI(clock=board.SCK, MISO=board.MISO, MOSI=board.MOSI)
    cs = digitalio.DigitalInOut(board.D5)
    mcp = MCP.MCP3008(spi, cs)
    channel = AnalogIn(mcp, MCP.P0)

    GPIO.setmode(GPIO.BCM)
    GPIO.setup(25, GPIO.IN)
    vcount = 1
    v = 0
    avg_volt = 0

    fastingCheck = database.child("Model").child("001").child("Fasting").get()
    fasting = fastingCheck.val()

    print("Measuring Blood Glucose Now : ")
    sleep(3)

    #Setup
    while vcount <= 5:
        sensor = GPIO.input(25)
        
        if sensor == 1:   # There is no signal
            print("No Object is Detected")
            sleep(1)
        elif sensor == 0: # There is a signal
            print("Finger Detected")
            v += channel.voltage
            print('ADC Voltage: ' + str(channel.voltage) + ' V')
            print("")
            vcount += 1
            sleep(3)

    avg_volt = v / 5.0

    glucose = 3.3 * avg_volt * 18.0182
    glucose_rounded = round(glucose, 2)
    if fasting == False:
        glucose_rounded += 40

    print(str(glucose_rounded) + " mg/dl")
    database.child("Model").child("001").child("Glucose").set(glucose_rounded)
    GPIO.cleanup()

def heart():

    from pulsesensor import Pulsesensor
    import time

    count = 1
    heartTotal = 0
    avgHeart = 0
    
    p = Pulsesensor()
    p.startAsyncBPM()

    print("Measuring Heart Rate Now : ")
    sleep(3)

    try:
        while count <= 5:
            bpm = p.BPM
            if bpm > 0:
                print("BPM: %d" % bpm)
                heartTotal += bpm
                count += 1
            else:
                print("No Heartbeat found")
            time.sleep(1)
    except:
        p.stopAsyncBPM()

    avgHeart = round(heartTotal / 5)
    print("Final Detected Heart Rate : ", avgHeart)

def o2():

    import board
    import busio
    import digitalio
    import RPi.GPIO as GPIO

    GPIO.cleanup()
    
    import max30102
    import hrcalc

    count = 1
    o2Total = 0
    heartTotal = 0
    avgO2 = 0
    avgHeart = 0

    print("Measuring Heart Rate and O2 Now : ")
    sleep(3)
    
    m = max30102.MAX30102()
    hr2 = 0
    sp2 = 0

    while count <= 5:
        red, ir = m.read_sequential()   
        hr,hrb,sp,spb = hrcalc.calc_hr_and_spo2(ir, red)

        print("hr detected:",hrb)
        print("sp detected:",spb)
        
        if(hrb == True and hr != -999):
            hr2 = int(hr)
            print("Heart Rate : ", hr2)
        if(spb == True and sp != -999):
            sp2 = int(sp)
            print("SPO2       : ", sp2)

        if (hr2 > 50 and sp2 > 70 and hr2 < 140):
            o2Total += sp2
            heartTotal += hr2
            count += 1

    avgO2 = round(o2Total / 5)
    avgHeart = round(heartTotal / 5)

    print("Final Detected Heart Rate : ", avgHeart)
    print("Final Detected 02 Saturation : ", avgO2)
    database.child("Model").child("001").child("Heart Rate").set(avgHeart)
    database.child("Model").child("001").child("O2").set(avgO2)

def startO2():

    import RPi.GPIO as GPIO
    
    import max30102
    import hrcalc

    count = 1
    o2Total = 0
    heartTotal = 0
    avgO2 = 0
    avgHeart = 0

    print("Measuring Heart Rate and O2 Now : ")
    sleep(3)
    
    m = max30102.MAX30102()
    hr2 = 0
    sp2 = 0

    while count <= 5:
        red, ir = m.read_sequential()   
        hr,hrb,sp,spb = hrcalc.calc_hr_and_spo2(ir, red)

        print("hr detected:",hrb)
        print("sp detected:",spb)
        
        if(hrb == True and hr != -999):
            hr2 = int(hr)
            print("Heart Rate : ", hr2)
        if(spb == True and sp != -999):
            sp2 = int(sp)
            print("SPO2       : ", sp2)

        if (hr2 > 50 and sp2 > 70 and hr2 < 140):
            o2Total += sp2
            heartTotal += hr2
            count += 1

    avgO2 = round(o2Total / 5)
    avgHeart = round(heartTotal / 5)

    print("Final Detected Heart Rate : ", avgHeart)
    print("Final Detected 02 Saturation : ", avgO2)
    database.child("Model").child("001").child("Heart Rate").set(avgHeart)
    database.child("Model").child("001").child("O2").set(avgO2)

    GPIO.cleanup()
 
while True:

    conductCheckup = database.child("Model").child("001").child("Conduct Checkup").get()
    conduct_check = conductCheckup.val()
    
    compCheckup = database.child("Model").child("001").child("Complete Checkup").get()
    complete_check = compCheckup.val()
    
    heartCheckupVal = database.child("Model").child("001").child("Heart Checkup").get()
    heart_check = heartCheckupVal.val()
    
    o2CheckupVal = database.child("Model").child("001").child("O2 Checkup").get()
    o2_check = o2CheckupVal.val()
    
    tempCheckupVal = database.child("Model").child("001").child("Temperature Checkup").get()
    temp_check = tempCheckupVal.val()
    
    glucoseCheckupVal = database.child("Model").child("001").child("Glucose Checkup").get()
    glucose_check = glucoseCheckupVal.val()
    
    heartVal = database.child("Model").child("001").child("Heart Rate").get()
    heartBPM = heartVal.val()
    
    o2Val = database.child("Model").child("001").child("O2").get()
    oxyLevel = o2Val.val()
    
    tempVal = database.child("Model").child("001").child("Temperature").get()
    temperature = tempVal.val()
    
    glucoseVal = database.child("Model").child("001").child("Glucose").get()
    gluco = glucoseVal.val()

    userVal = database.child("Model").child("001").child("Username").get()
    username = userVal.val()
    
    if conduct_check == True:
        
        if complete_check == True:
            
            if (heart_check == 1 and o2_check == 1 and temp_check == -1 and glucose_check == -1):
                startO2()
                heart_check = 0
                o2_check = 0
                database.child("Model").child("001").child("Heart Checkup").set(heart_check)
                database.child("Model").child("001").child("O2 Checkup").set(o2_check)

            if (heart_check == 0 and o2_check == 0 and temp_check == -1 and glucose_check == 1):
                glucose()
                glucose_check = 0
                database.child("Model").child("001").child("Glucose Checkup").set(glucose_check)

            if (heart_check == 0 and o2_check == 0 and temp_check == 1 and glucose_check == 0):
                temp()
                temp_check = 0
                database.child("Model").child("001").child("Temperature Checkup").set(temp_check)
                makePrediction(username)
                database.child("Model").child("001").child("Conduct Checkup").set(False)
                print("Overall Checkup Complete")

            print("Complete Check on Going")
            
        else:

            if (heart_check == 1 and o2_check == 1 and temp_check == -1 and glucose_check == -1):
                startO2()
                heart_check = 0
                o2_check = 0
                database.child("Model").child("001").child("Heart Checkup").set(heart_check)
                database.child("Model").child("001").child("O2 Checkup").set(o2_check)

            if (heart_check == 0 and o2_check == 0 and temp_check == -1 and glucose_check == 1):
                glucose()
                glucose_check = 0
                database.child("Model").child("001").child("Glucose Checkup").set(glucose_check)

            if (heart_check == 0 and o2_check == 0 and temp_check == 1 and glucose_check == 0):
                temp()
                temp_check = 0
                database.child("Model").child("001").child("Temperature Checkup").set(temp_check)
                makePrediction(username)
                database.child("Model").child("001").child("Conduct Checkup").set(False)
                print("Overall Checkup Complete")

            if (heart_check == -1 and o2_check == -1 and temp_check == 1 and glucose_check == -1):
                temp()
                temp_check = 0
                database.child("Model").child("001").child("Temperature Checkup").set(temp_check)

            if (heart_check == -1 and o2_check == -1 and temp_check == 0 and glucose_check == 1):
                glucose()
                glucose_check = 0
                database.child("Model").child("001").child("Glucose Checkup").set(glucose_check)

            if (heart_check == 1 and o2_check == 1 and temp_check == 0 and glucose_check == 0):
                startO2()
                heart_check = 0
                o2_check = 0
                database.child("Model").child("001").child("Heart Checkup").set(heart_check)
                database.child("Model").child("001").child("O2 Checkup").set(o2_check)
                makePrediction(username)
                database.child("Model").child("001").child("Conduct Checkup").set(False)
                print("Overall Checkup Complete")

            if (heart_check == -1 and o2_check == -1 and temp_check == -1 and glucose_check == 1):
                glucose()
                glucose_check = 0
                database.child("Model").child("001").child("Glucose Checkup").set(glucose_check)

            if (heart_check == 1 and o2_check == 1 and temp_check == -1 and glucose_check == 0):
                startO2()
                heart_check = 0
                o2_check = 0
                database.child("Model").child("001").child("Heart Checkup").set(heart_check)
                database.child("Model").child("001").child("O2 Checkup").set(o2_check)
                
    else:

        print("Checkup Done / Not Going On")

            
        
                              





