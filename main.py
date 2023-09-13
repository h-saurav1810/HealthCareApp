import yaml
def load_responses():
    with open('responses.yaml', 'r') as file:
        return yaml.safe_load(file)


def get_response(user_input, responses):
    user_input = user_input.lower()
    if user_input in responses:
        return responses[user_input]
    return "I'm sorry, I don't understand. Please answer with 'yes' or 'no'."


def main():
    responses = load_responses()

    print("Chatbot: Hi! I'm a simple chatbot. You can ask me 'yes' or 'no' questions.")
    while True:
        user_input = input("You: ")
        if user_input.lower() == 'exit':
            print("Chatbot: Goodbye!")
            break
        response = get_response(user_input, responses)
        print("Chatbot:", response)

import re
symptomfile = open("Symptoms.txt", "r")

age = 21
sex = "Male"

height = 167
weight = 53.2
bmi = weight / pow((height/100), 2)

family_history = "nothing"
current_problem = "no"
not_fasting = False

temperature = 37.9
heart_rate = 119
glucose = 128.67
oxygen = 92

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

if(temperature >= 36.4 and temperature <= 37.5):
    inchealthyScore()
if(oxygen >= 95):
    inchealthyScore()
if (heart_rate >= 60 and heart_rate <= 100):
    inchealthyScore()
if(not_fasting == True):
    if(glucose >= 120 and glucose <= 180):
        inchealthyScore()
else:
    if (glucose >= 80 and glucose <= 120):
        inchealthyScore()

if(temperature >= 37.5):
    incfeverScore()
if(oxygen <= 96):
    incfeverScore()
if (heart_rate >= 90):
    incfeverScore()
if(not_fasting == True):
    if(glucose >= 120 and glucose <= 180):
        incfeverScore()
else:
    if (glucose >= 80 and glucose <= 120):
        incfeverScore()

if(temperature > 37.8 or temperature < 36.2):
    incfluScore()
if(oxygen < 95):
    incfluScore()
if(sex == "Male"):
    if (heart_rate > 100):
        incfluScore()
elif (sex == "Female"):
    if (heart_rate > 82):
        incfluScore()
if (not_fasting == True):
    if (glucose >= 120 and glucose <= 180):
        incfluScore()
else:
    if (glucose >= 80 and glucose <= 120):
        incfluScore()

if(temperature > 37.8):
    inccovidScore()
if(oxygen < 92):
    inccovidScore()
if(sex == "Male"):
    if (heart_rate > 100):
        inccovidScore()
elif (sex == "Female"):
    if (heart_rate > 82):
        inccovidScore()
if (not_fasting == True):
    if (glucose >= 120 and glucose <= 180):
        inccovidScore()
else:
    if (glucose >= 80 and glucose <= 120):
        inccovidScore()

if(temperature > 37.8 or temperature < 36.2):
    inccoldScore()
if(oxygen < 97):
    inccoldScore()
if(sex == "Male"):
    if (heart_rate > 100):
        inccoldScore()
elif (sex == "Female"):
    if (heart_rate > 82):
        inccoldScore()
if (not_fasting == True):
    if (glucose >= 120 and glucose <= 180):
        inccoldScore()
else:
    if (glucose >= 80 and glucose <= 120):
        inccoldScore()

if(temperature > 37.8):
    incasthmaScore()
if(oxygen < 90):
    incasthmaScore()
if(sex == "Male"):
    if (heart_rate > 110):
        incasthmaScore()
elif (sex == "Female"):
    if (heart_rate > 90):
        incasthmaScore()
if (not_fasting == True):
    if (glucose >= 120 and glucose <= 180):
         incasthmaScore()
else:
    if (glucose >= 80 and glucose <= 120):
        incasthmaScore()

if(temperature > 37.6):
    inchyperglycemiaScore()
if(oxygen >= 95):
    inchyperglycemiaScore()
if (heart_rate > 88):
    inchyperglycemiaScore()
if (not_fasting == True):
    if (glucose > 180):
        inchyperglycemiaScore()
else:
    if (glucose > 120):
        inchyperglycemiaScore()

if(temperature > 37.6):
    incprediabetesScore()
if(oxygen >= 95):
    incprediabetesScore()
if (heart_rate > 88):
    incprediabetesScore()
if (not_fasting == True):
    if (glucose >= 150 and glucose <= 180):
        incprediabetesScore()
else:
    if (glucose >= 110 and glucose <= 140):
        incprediabetesScore()

if(temperature > 37.6):
    inchypoglycemiaScore()
if(oxygen <= 94):
    inchypoglycemiaScore()
if (heart_rate > 90):
    inchypoglycemiaScore()
if (not_fasting == True):
    if (glucose <= 145):
        inchypoglycemiaScore()
else:
    if (glucose <= 75):
        inchypoglycemiaScore()

if(temperature > 38.4):
    incpneumoniaScore()
if(oxygen <= 92):
    incpneumoniaScore()
if (heart_rate > 112):
    incpneumoniaScore()
if (not_fasting == True):
    if (glucose >= 130):
        incpneumoniaScore()
else:
    if (glucose >= 95):
        incpneumoniaScore()

if(temperature >= 36 and temperature <= 38):
    incstressScore()
if(oxygen >= 95 and oxygen <= 98):
    incstressScore()
if (heart_rate >= 125):
    incstressScore()
if(not_fasting == True):
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
if (not_fasting == True):
    if (glucose >= 120 and glucose <= 180):
        incarrhythmiaScore()
else:
    if (glucose >= 80 and glucose <= 120):
        incarrhythmiaScore()

if (bmi > 25) or (re.findall("Diabetes", current_problem)) or (re.findall("Diabetes", family_history)):
    inccoronaryScore()
if(temperature >= 38 and temperature < 36.3):
    inccoronaryScore()
if(oxygen <= 93):
    inccoronaryScore()
if (heart_rate >= 60 and heart_rate <= 75):
    inccoronaryScore()
if(not_fasting == True):
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
if (not_fasting == True):
    if (glucose > 170):
        inclungcancerScore()
else:
    if (glucose > 112.5):
        inclungcancerScore()

if(sex == "Female"):
    if (temperature > 38.2):
        incbreastcancerScore()
    if (oxygen < 90):
        incbreastcancerScore()
    if (heart_rate > 90):
        incbreastcancerScore()
    if (not_fasting == True):
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
if (not_fasting == True):
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

file1 = open("Symptoms.txt", "r")
sympline = []
for oneline in file1:
    found = identified_diagnosis
    if(re.findall(found, oneline)):
        print(oneline)
        cleaned_line = oneline.replace(found + ":", "")
        cleaned_line = cleaned_line.replace("\n", "")
        sympline.append(re.split(",", cleaned_line))

symptoms = []
symptom_score = 0
final_high = medibase[0][1]
diagnosis = medibase[0][0]
solution = medibase[0][2]

if (sympline[0][0] == ""):
    symptom = input("Have you been experiencing any kind of symptoms?")
    if ((re.findall("No", symptom)) or (re.findall("not been", symptom))):
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
    elif((re.findall("Yes", symptom)) or (re.findall("have been", symptom))):
        sympline.clear()
        filecount = 0
        symptom = input("OH! Can you please describe the symptoms you are having?")
        for line in symptomfile:
            filecount += 1
            for i in range(len(medibase)):
                if (medibase[i][3] == filecount):
                    cleaned_line = line.replace(medibase[i][0] + ":", "")
                    cleaned_line = cleaned_line.replace("\n", "")
                    sympline.append(re.split(",", cleaned_line))
        for i in range(len(sympline)):
            for j in range(len(sympline[i])):
                if (re.findall(sympline[i][j], symptom) and (sympline[i][j] != "")):
                    medibase[i][5] += 1
        for i in range(len(medibase)):
            symptom_score = (medibase[i][5] / medibase[i][4]) * 50
            medibase[i][1] += symptom_score
        for i in range(len(medibase)):
            if (medibase[i][1] > final_high):
                final_high = medibase[i][1]
                diagnosis = medibase[i][0]
                solution = medibase[i][2]
else:
    index = 0
    not_symptom = 0
    while (index + 1 <= len(sympline[0])):
        symptom = input("Have you been been suffering from " + sympline[0][index])
        if ((re.findall("Yes", symptom)) or (re.findall("have been", symptom))):
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
        elif((re.findall("No", symptom)) or (re.findall("not been", symptom))):
            not_symptom += 1
            if (not_symptom < 3):
                print("Alright then")
            else:
                sympline.clear()
                filecount = 0
                symptom = input("Can you then please describe the symptoms you are having?")
                for line in symptomfile:
                    filecount += 1
                    for i in range(len(medibase)):
                        if (medibase[i][3] == filecount):
                            cleaned_line = line.replace(medibase[i][0] + ":", "")
                            cleaned_line = cleaned_line.replace("\n", "")
                            sympline.append(re.split(",", cleaned_line))
                for i in range(len(sympline)):
                    for j in range(len(sympline[i])):
                        if (re.findall(sympline[i][j], symptom) and (sympline[i][j] != "")):
                            medibase[i][5] += 1
                for i in range(len(medibase)):
                    symptom_score = (medibase[i][5] / medibase[i][4]) * 50
                    medibase[i][1] += symptom_score
        index += 1
    for i in range(len(medibase)):
        if (medibase[i][1] > final_high):
            final_high = medibase[i][1]
            diagnosis = medibase[i][0]
            solution = medibase[i][2]



