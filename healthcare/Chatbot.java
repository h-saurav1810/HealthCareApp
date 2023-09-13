package com.example.healthcare;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.healthcare.databinding.ActivityChatbotBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chatbot extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://healthcare-c3b0a-default-rtdb.firebaseio.com/");
    final Handler handler = new Handler();
    String reply;
    String choice;
    String fastingChoice;
    String checkupContinue;

    String symptomResponse;
    String questionAsked = "";
    String symptomQuestion;
    String symptomAnswer;
    String diagnosis;
    String recommendation;

    Boolean compPrediction;
    Boolean compCheckup;
    Boolean conductCheckup;
    Integer heart;
    Integer temp;
    Integer glucose;
    Integer o2;

    Double glucoseValue;
    Double tempValue;
    Integer heartValue;
    Integer o2Value;

    private RecyclerView chats;
    private EditText messageBar;
    private ImageView sendMsgBtn;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatModel> chatModelArrayList;
    private MessageAdapter messageAdapter;

    String username;
    String checkupModel;
    String[] checkupYes = {"yes", "i do", "want to", "would", "i'd", "like"};
    String[] checkupNo = {"no", "i do not", "don't", "would not", "not", "wouldn't"};
    Integer checkup = -1;
    Integer indCheckup = -1;
    Integer contCheckup = -1;

    Boolean questionCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        username = getIntent().getStringExtra("user");
        String fullName = getIntent().getStringExtra("name");
        String birthdate = getIntent().getStringExtra("dob");
        String mail = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("pwd");
        String gender = getIntent().getStringExtra("sex");
        String bodyHeight = getIntent().getStringExtra("height");
        String bodyWeight = getIntent().getStringExtra("weight");
        String healthIssues = getIntent().getStringExtra("issues");
        String healthMedication = getIntent().getStringExtra("medication");
        checkupModel = getIntent().getStringExtra("model");
        String lastHeart = getIntent().getStringExtra("last_heart");
        String lastO2 = getIntent().getStringExtra("last_o2");
        String lastTemp = getIntent().getStringExtra("last_temp");
        String lastGlucose = getIntent().getStringExtra("last_glucose");
        String lastDiagnosis = getIntent().getStringExtra("last_diagnosis");
        String lastRecommendation = getIntent().getStringExtra("last_recommendation");

        Intent homeIntent = new Intent(Chatbot.this, Homepage.class);
        homeIntent.putExtra("user", username);
        homeIntent.putExtra("name", fullName);
        homeIntent.putExtra("dob", birthdate);
        homeIntent.putExtra("email", mail);
        homeIntent.putExtra("pwd", password);
        homeIntent.putExtra("sex", gender);
        homeIntent.putExtra("height", bodyHeight);
        homeIntent.putExtra("weight", bodyWeight);
        homeIntent.putExtra("issues", healthIssues);
        homeIntent.putExtra("medication", healthMedication);
        homeIntent.putExtra("model", checkupModel);
        homeIntent.putExtra("last_heart", lastHeart);
        homeIntent.putExtra("last_o2", lastO2);
        homeIntent.putExtra("last_temp", lastTemp);
        homeIntent.putExtra("last_glucose", lastGlucose);
        homeIntent.putExtra("last_diagnosis", lastDiagnosis);
        homeIntent.putExtra("last_recommendation", lastRecommendation);

        chats = findViewById(R.id.chatRV);
        messageBar = findViewById(R.id.sendMessage);
        sendMsgBtn = findViewById(R.id.sendBtn);
        chatModelArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(chatModelArrayList, this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chats.setLayoutManager(manager);
        chats.setAdapter(messageAdapter);

        databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(checkupModel)) {
                    heart = snapshot.child(checkupModel).child("Heart Checkup").getValue(Integer.class);
                    temp = snapshot.child(checkupModel).child("Temperature Checkup").getValue(Integer.class);
                    glucose = snapshot.child(checkupModel).child("Glucose Checkup").getValue(Integer.class);
                    o2 = snapshot.child(checkupModel).child("O2 Checkup").getValue(Integer.class);
                    compCheckup = snapshot.child(checkupModel).child("Complete Checkup").getValue(Boolean.class);
                    conductCheckup = snapshot.child(checkupModel).child("Conduct Checkup").getValue(Boolean.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        startBot();

        ImageView home;
        home = findViewById(R.id.homeBtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(homeIntent);
            }

        });

    }

    private void getResponse(String message) {
        chatModelArrayList.add(new ChatModel(message, USER_KEY));
        messageAdapter.notifyDataSetChanged();

        for (int i = 0; i < checkupYes.length; i++) {
            if (message.toLowerCase().contains(checkupYes[i])) {
                checkup = 1;
            }
        }

        for (int i = 0; i < checkupNo.length; i++) {
            if (message.toLowerCase().contains(checkupNo[i])) {
                checkup = 0;
            }
        }

        if (checkup == 1) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Alright then! Let's start the checkup.", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 2000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkup();
                }
            }, 3000);

        } else if (checkup == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Alright then. Would you like to monitor specific body vitals?", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 2000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    indCheckup();
                }
            }, 3000);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("I am sorry but I do not have enough information to reply :(", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 2000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startBot();
                }
            }, 3000);
        }


    }

    private void startBot() {

        databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Model").child(checkupModel).child("Username").setValue(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Hi! MediBot at your Service.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Would you like to conduct a complete health checkup today?", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 6000);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageBar.getText().toString().isEmpty()) {
                    Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(messageBar.getText().toString());
                messageBar.setText("");
            }
        });

    }

    private void checkup() {

        conductCheckup = true;
        compCheckup = true;
        databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Model").child(checkupModel).child("Conduct Checkup").setValue(conductCheckup);
                databaseReference.child("Model").child(checkupModel).child("Complete Checkup").setValue(compCheckup);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        heartO2Monitor();

    }

    private void indCheckup() {

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageBar.getText().toString().isEmpty()) {
                    Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                    return;
                }

                reply = messageBar.getText().toString();
                messageBar.setText("");

                chatModelArrayList.add(new ChatModel(reply, USER_KEY));
                messageAdapter.notifyDataSetChanged();

                for (int i = 0; i < checkupYes.length; i++) {
                    if (reply.toLowerCase().contains(checkupYes[i])) {
                        indCheckup = 1;
                    }
                }

                for (int i = 0; i < checkupNo.length; i++) {
                    if (reply.toLowerCase().contains(checkupNo[i])) {
                        indCheckup = 0;
                    }
                }

                if (indCheckup == 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Great! Which body vital would you like to measure now?", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("1. Heart Rate & Oxygen Level", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 3000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("2. Body Temperature", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 4000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("3. Glucose", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 5000);


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chooseMonitor();
                        }
                    }, 6000);

                } else if (indCheckup == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Ohh! I hope I can help you next time then. Bye! ", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 3000);
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("I am sorry but I do not have enough information to reply :(", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Would you still like to monitor specific body vitals?", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 4000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            indCheckup();
                        }
                    }, 5000);
                }

            }
        });

    }

    private void chooseMonitor() {

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageBar.getText().toString().isEmpty()) {
                    Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                    return;
                }

                choice = messageBar.getText().toString();
                messageBar.setText("");

                chatModelArrayList.add(new ChatModel(choice, USER_KEY));
                messageAdapter.notifyDataSetChanged();

                if ((choice.toLowerCase().contains("1")) || (choice.toLowerCase().contains("heart")) || (choice.toLowerCase().contains("heart and oxygen")) || (choice.toLowerCase().contains("oxygen"))) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            heartO2Monitor();
                        }
                    }, 2000);
                } else if ((choice.toLowerCase().contains("2")) || (choice.toLowerCase().contains("temperature"))) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tempMonitor();
                        }
                    }, 2000);
                } else if ((choice.toLowerCase().contains("3")) || (choice.toLowerCase().contains("glucose"))) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            glucoseMonitor();
                        }
                    }, 2000);
                }

            }
        });

    }

    private void heartO2Monitor() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Okay. Let's measure your heart rate and blood oxygen level.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Place your index finger on the Pulse Sensor.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 4500);

        if (heart == -1 && o2 == -1) {
            heart = 1;
            o2 = 1;
            conductCheckup = true;

            databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child("Model").child(checkupModel).child("Conduct Checkup").setValue(conductCheckup);
                    databaseReference.child("Model").child(checkupModel).child("Heart Checkup").setValue(heart);
                    databaseReference.child("Model").child(checkupModel).child("O2 Checkup").setValue(o2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.d(TAG, "SECOND HERE");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    heartO2Wait();
                }
            }, 6000);
        }
    }

    private void tempMonitor() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Okay. Let's measure your body temperature.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Hold the temperature sensor at your head / mouth.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 4500);

        if (temp == -1) {
            temp = 1;
            int localTemp = temp;
            conductCheckup = true;

            databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    databaseReference.child("Model").child(checkupModel).child("Conduct Checkup").setValue(conductCheckup);
                    databaseReference.child("Model").child(checkupModel).child("Temperature Checkup").setValue(localTemp);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tempWait();
                }
            }, 7000);
        }

    }

    private void glucoseMonitor() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Okay. Let's measure your glucose level now.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Before measuring, Have you eaten anything in the last 2 hours?", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 4500);

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (messageBar.getText().toString().isEmpty()) {
                    Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                    return;
                }

                fastingChoice = messageBar.getText().toString();
                messageBar.setText("");

                chatModelArrayList.add(new ChatModel(fastingChoice, USER_KEY));
                messageAdapter.notifyDataSetChanged();

                if ((fastingChoice.toLowerCase().contains("yes")) || (fastingChoice.toLowerCase().contains("have"))) {
                    databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("Model").child(checkupModel).child("Fasting").setValue(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if ((fastingChoice.toLowerCase().contains("no")) || (fastingChoice.toLowerCase().contains("have not"))) {
                    databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("Model").child(checkupModel).child("Fasting").setValue(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chatModelArrayList.add(new ChatModel("Hold your index finger close to the IR Glucose Sensor.", BOT_KEY));
                        messageAdapter.notifyDataSetChanged();
                    }
                }, 7500);

                if (glucose == -1) {
                    glucose = 1;
                    conductCheckup = true;

                    databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.child("Model").child(checkupModel).child("Conduct Checkup").setValue(conductCheckup);
                            databaseReference.child("Model").child(checkupModel).child("Glucose Checkup").setValue(glucose);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            glucoseWait();
                        }
                    }, 9000);
                }
            }
        });

    }

    private void heartO2Wait() {

        retrieveParam();

        if (heart != 0 && o2 != 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Monitoring Heart Rate and Oxygen Level .....", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 3000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    heartO2Wait();
                }
            }, 4500);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Heart Rate : " + heartValue + " BPM", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 3000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Oxygen Level : " + o2Value + " %", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 4500);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    completeCheckup(heart, temp, glucose, o2);
                }
            }, 5500);
        }
    }

    private void tempWait() {

        retrieveParam();

        if (temp != 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Monitoring Body Temperature .....", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 3000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tempWait();
                }
            }, 4500);
        }

        if (temp == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Body Temperature : " + tempValue + " ⁰C", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 2500);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    completeCheckup(heart, temp, glucose, o2);
                }
            }, 3500);
        }

    }

    private void glucoseWait() {

        retrieveParam();
        if (glucose != 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Monitoring Blood Glucose Level .....", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 3000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    glucoseWait();
                }
            }, 4500);

        }

        if (glucose == 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    chatModelArrayList.add(new ChatModel("Blood Glucose Level : " + glucoseValue + " mg/dl", BOT_KEY));
                    messageAdapter.notifyDataSetChanged();
                }
            }, 2500);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    completeCheckup(heart, temp, glucose, o2);
                }
            }, 4000);
        }

    }

    private void completeCheckup(Integer heartCheck, Integer tempCheck, Integer glucoseCheck, Integer o2Check) {

        retrieveParam();
        if (compCheckup.equals(true)) {
            if (heartCheck == 0 && tempCheck == 0 && glucoseCheck == 0 && o2Check == 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPrediction();
                    }
                }, 2000);
            }
            if (heartCheck == 0 && tempCheck == -1 && glucoseCheck == -1 && o2Check == 0) {
                glucoseMonitor();
            }
            if (heartCheck == 0 && tempCheck == -1 && glucoseCheck == 0 && o2Check == 0) {
                tempMonitor();
            }
        } else {

            if (heartCheck == 0 && tempCheck == 0 && glucoseCheck == 0 && o2Check == 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPrediction();
                    }
                }, 2000);
            }

            if (heartCheck == 0 && o2Check == 0) {
                if (tempCheck == -1 && glucoseCheck == -1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Would you like to monitor the other body vitals?", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                    sendMsgBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (messageBar.getText().toString().isEmpty()) {
                                Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                checkupContinue = messageBar.getText().toString();
                                messageBar.setText("");

                                chatModelArrayList.add(new ChatModel(checkupContinue, USER_KEY));
                                messageAdapter.notifyDataSetChanged();

                                for (int i = 0; i < checkupYes.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupYes[i])) {
                                        contCheckup = 1;
                                    }
                                }

                                for (int i = 0; i < checkupNo.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupNo[i])) {
                                        contCheckup = 0;
                                    }
                                }

                                if (contCheckup == 1) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            glucoseMonitor();
                                        }
                                    }, 2000);
                                } else {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    databaseReference.child("Model").child("001").child("Temperature").setValue(0.0);
                                                    databaseReference.child("Model").child("001").child("Glucose").setValue(0.0);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }, 2000);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            endMediBot();
                                        }
                                    }, 3500);
                                }
                            }
                        }
                    });

                } else if (tempCheck == -1 && glucoseCheck == 0) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tempMonitor();
                        }
                    }, 3000);
                }
            }

            if (tempCheck == 0) {
                if (heartCheck == -1 && glucoseCheck == -1 && o2Check == -1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Would you like to monitor the other body vitals?", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                    sendMsgBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (messageBar.getText().toString().isEmpty()) {
                                Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                            } else {
                                checkupContinue = messageBar.getText().toString();
                                messageBar.setText("");

                                chatModelArrayList.add(new ChatModel(checkupContinue, USER_KEY));
                                messageAdapter.notifyDataSetChanged();

                                for (int i = 0; i < checkupYes.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupYes[i])) {
                                        contCheckup = 1;
                                    }
                                }

                                for (int i = 0; i < checkupNo.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupNo[i])) {
                                        contCheckup = 0;
                                    }
                                }

                                if (contCheckup == 1) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            glucoseMonitor();
                                        }
                                    }, 2000);
                                } else {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    databaseReference.child("Model").child("001").child("Heart Rate").setValue(0);
                                                    databaseReference.child("Model").child("001").child("O2").setValue(0);
                                                    databaseReference.child("Model").child("001").child("Glucose").setValue(0.0);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }, 2000);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            endMediBot();
                                        }
                                    }, 3500);
                                }
                            }
                        }
                    });
                } else if (heartCheck == -1 && glucoseCheck == 0 && o2Check == -1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            heartO2Monitor();
                        }
                    }, 2000);
                }
            }

            if (glucoseCheck == 0) {
                if (heartCheck == -1 && tempCheck == -1 && o2Check == -1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chatModelArrayList.add(new ChatModel("Would you like to monitor the other body vitals and complete a health checkup?", BOT_KEY));
                            messageAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                    sendMsgBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (messageBar.getText().toString().isEmpty()) {
                                Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                            } else {
                                checkupContinue = messageBar.getText().toString();
                                messageBar.setText("");

                                chatModelArrayList.add(new ChatModel(checkupContinue, USER_KEY));
                                messageAdapter.notifyDataSetChanged();

                                for (int i = 0; i < checkupYes.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupYes[i])) {
                                        contCheckup = 1;
                                    }
                                }

                                for (int i = 0; i < checkupNo.length; i++) {
                                    if (checkupContinue.toLowerCase().contains(checkupNo[i])) {
                                        contCheckup = 0;
                                    }
                                }

                                if (contCheckup == 1) {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            heartO2Monitor();
                                        }
                                    }, 2000);
                                } else {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    databaseReference.child("Model").child("001").child("Heart Rate").setValue(0);
                                                    databaseReference.child("Model").child("001").child("O2").setValue(0);
                                                    databaseReference.child("Model").child("001").child("Temperature").setValue(0.0);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }, 2000);

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            endMediBot();
                                        }
                                    }, 3500);
                                }
                            }
                        }
                    });
                }
            }
        }

    }

    private void getPrediction() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(username)) {
                            diagnosis = snapshot.child(username).child("Last Diagnosis").getValue(String.class);
                            recommendation = snapshot.child(username).child("Last Recommendation").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Identified Diagnosis : " + diagnosis, BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 2500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Recommendation : " + recommendation, BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 4000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                endMediBot();
            }
        }, 6000);

    }

    private void endMediBot() {

        String formattedTime = "", formattedDate = "";
        LocalDateTime recordedTime, recordedDate;
        DateTimeFormatter timeFormat, dateFormat;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            recordedTime = LocalDateTime.now();
            timeFormat = DateTimeFormatter.ofPattern("HH-mm-ss");
            recordedDate = LocalDateTime.now();
            dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formattedTime = recordedTime.format(timeFormat);
            formattedDate = recordedDate.format(dateFormat);
        }

        retrieveParam();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("Monitoring is completed.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 2000);

        retrieveParam();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chatModelArrayList.add(new ChatModel("You can check the Complete Diagnosis from the Homepage.", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (heartValue != 0) {
                            databaseReference.child("Users").child(username).child("Last Heart").setValue(heartValue);
                        }
                        if (o2Value != 0) {
                            databaseReference.child("Users").child(username).child("Last O2").setValue(o2Value);
                        }
                        if (tempValue != 0.0) {
                            databaseReference.child("Users").child(username).child("Last Temp").setValue(tempValue);
                        }
                        if (glucoseValue != 0.0) {
                            databaseReference.child("Users").child(username).child("Last Glucose").setValue(glucoseValue);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 4000);

        String finalFormattedDate = formattedDate;
        String finalFormattedTime = formattedTime;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference.child("Monitored Data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("Monitored Data").child(username).child(finalFormattedDate).child(finalFormattedTime).child("Heart Rate").setValue(heartValue);
                        databaseReference.child("Monitored Data").child(username).child(finalFormattedDate).child(finalFormattedTime).child("O2").setValue(o2Value);
                        databaseReference.child("Monitored Data").child(username).child(finalFormattedDate).child(finalFormattedTime).child("Temperature").setValue(tempValue);
                        databaseReference.child("Monitored Data").child(username).child(finalFormattedDate).child(finalFormattedTime).child("Glucose").setValue(glucoseValue);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 5000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setFinalParam();
            }
        }, 6000);

    }

    private void retrieveParam() {

        databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(checkupModel)) {
                    heart = snapshot.child(checkupModel).child("Heart Checkup").getValue(Integer.class);
                    temp = snapshot.child(checkupModel).child("Temperature Checkup").getValue(Integer.class);
                    glucose = snapshot.child(checkupModel).child("Glucose Checkup").getValue(Integer.class);
                    o2 = snapshot.child(checkupModel).child("O2 Checkup").getValue(Integer.class);
                    heartValue = snapshot.child(checkupModel).child("Heart Rate").getValue(Integer.class);
                    o2Value = snapshot.child(checkupModel).child("O2").getValue(Integer.class);
                    tempValue = snapshot.child(checkupModel).child("Temperature").getValue(Double.class);
                    glucoseValue = snapshot.child(checkupModel).child("Glucose").getValue(Double.class);
                    compCheckup = snapshot.child(checkupModel).child("Complete Checkup").getValue(Boolean.class);
                    conductCheckup = snapshot.child(checkupModel).child("Conduct Checkup").getValue(Boolean.class);
                    compPrediction = snapshot.child(checkupModel).child("Complete Prediction").getValue(Boolean.class);
                    symptomQuestion = snapshot.child(checkupModel).child("Symptom Question").getValue(String.class);
                    symptomAnswer = snapshot.child(checkupModel).child("Symptom Answer").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void setFinalParam() {

        databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Model").child(checkupModel).child("Conduct Checkup").setValue(false);
                databaseReference.child("Model").child(checkupModel).child("Complete Checkup").setValue(false);
                databaseReference.child("Model").child(checkupModel).child("Complete Prediction").setValue(false);
                databaseReference.child("Model").child(checkupModel).child("Fasting").setValue(true);
                databaseReference.child("Model").child(checkupModel).child("Heart Checkup").setValue(-1);
                databaseReference.child("Model").child(checkupModel).child("Temperature Checkup").setValue(-1);
                databaseReference.child("Model").child(checkupModel).child("Glucose Checkup").setValue(-1);
                databaseReference.child("Model").child(checkupModel).child("O2 Checkup").setValue(-1);
                databaseReference.child("Model").child(checkupModel).child("Symptom Question").setValue("");
                databaseReference.child("Model").child(checkupModel).child("Symptom Answer").setValue("");
                databaseReference.child("Model").child(checkupModel).child("Username").setValue("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setPrediction() {

        Log.d(TAG, questionAsked);
        Log.d(TAG, symptomQuestion);

        if (compPrediction == false)
        {
            /*
            if (questionAsked.equals(symptomQuestion))
            {
                questionCheck = true;
            }
            */
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // if (questionCheck.equals(false))
                    // {
                        if (!symptomQuestion.isEmpty())
                        {
                            if (questionAsked.equals(symptomQuestion))
                            {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        retrieveParam();
                                        setPrediction();
                                    }
                                }, 1500);
                            }
                            else if (symptomQuestion.equals("Hmm, Alright Then"))
                            {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatModelArrayList.add(new ChatModel(symptomQuestion, BOT_KEY));
                                        messageAdapter.notifyDataSetChanged();
                                    }
                                }, 3000);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        retrieveParam();
                                        setPrediction();
                                    }
                                }, 4500);
                            }
                            else {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        chatModelArrayList.add(new ChatModel(symptomQuestion, BOT_KEY));
                                        messageAdapter.notifyDataSetChanged();
                                        questionAsked = symptomQuestion;
                                    }
                                }, 2000);

                                sendMsgBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (messageBar.getText().toString().isEmpty()) {
                                            Toast.makeText(Chatbot.this, "Enter your message to communicate with the MediBot", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            symptomResponse = messageBar.getText().toString();
                                            messageBar.setText("");

                                            chatModelArrayList.add(new ChatModel(symptomResponse, USER_KEY));
                                            messageAdapter.notifyDataSetChanged();

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    databaseReference.child("Model").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            databaseReference.child("Model").child(checkupModel).child("Symptom Answer").setValue(symptomResponse);
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    retrieveParam();
                                                    setPrediction();
                                                }
                                            }, 4000);

                                        }
                                    }
                                });
                            }
                        }
                        else
                        {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    retrieveParam();
                                    setPrediction();
                                }
                            }, 2500);
                        }

                    // }

                }

                }, 5000);
/*
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setPrediction();
                }
            }, 4500);
*/
        }

        if (compPrediction == true)
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getPrediction();
                }
            }, 2500);
        }

    }
}