#!/usr/bin/env python
# coding: utf-8

# In[2]:


# Nicholas Whisman: 29 Jan 2021
# This Script takes the preprocessor and processor from the 8k Audio project and mashes them into one script
#
# Ideally, end-game, this script will look over the audio files in the server (be it by file system or database)
# analyze them, and train/re-train the model completely over again. 

#-----------------------------------------------------------------------------------------------------------#
# Import libraries and dependencies. ML is scary, so there's a lot. Keep it simple.

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
from tqdm import tqdm
from librosa import display
import librosa
from tensorflow.keras.utils import to_categorical
from numpy import genfromtxt
from tensorflow.keras import Sequential
import tensorflow as tf
from tensorflow.keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Dropout
from tensorflow.keras import Model


# In[6]:


###PRE-PROCESSOR###
#-----------------------------------------------------------------#

#forming a panda dataframe from the metadata file
data=pd.read_csv("UrbanSound8K/metadata/UrbanSound8K.csv")

#head of the dataframe
data.head()

#count of datapoints in each of the folders
data["fold"].value_counts()

#preprocessing using only mfcc
x_train=[]
x_test=[]
y_train=[]
y_test=[]
path="UrbanSound8K/audio/fold"
for i in tqdm(range(len(data))):
    fold_no=str(data.iloc[i]["fold"])
    file=data.iloc[i]["slice_file_name"]
    label=data.iloc[i]["classID"]
    filename=path+fold_no+"/"+file
    #print(filename)
    y,sr=librosa.load(filename)
    mfccs = np.mean(librosa.feature.mfcc(y, sr, n_mfcc=40).T,axis=0)
    #print(mfccs.shape,mfccs.max(),mfccs.min())
    if(fold_no!='10'):
      x_train.append(mfccs)
      y_train.append(label)
    else:
      x_test.append(mfccs)
      y_test.append(label)
        
len(x_train)+len(x_test)
len(data)

#converting the lists into numpy arrays
x_train=np.array(x_train)
x_test=np.array(x_test)
y_train=np.array(y_train)
y_test=np.array(y_test)
x_train.shape,x_test.shape,y_train.shape,y_test.shape

#saving the data numpy arrays
np.savetxt("train_data.csv", x_train, delimiter=",")
np.savetxt("test_data.csv",x_test,delimiter=",")
np.savetxt("train_labels.csv",y_train,delimiter=",")
np.savetxt("test_labels.csv",y_test,delimiter=",")

x_test[0].shape


# In[7]:


### Processing/ Training ###
#------------------------------------------------#

#extracting data from csv files into numpy arrays
x_train = genfromtxt('train_data.csv', delimiter=',')
y_train = genfromtxt('train_labels.csv', delimiter=',')
x_test = genfromtxt('test_data.csv', delimiter=',')
y_test = genfromtxt('test_labels.csv', delimiter=',')

#shape
x_train.shape,x_test.shape,y_train.shape,y_test.shape

#converting to one hot
y_train = to_categorical(y_train, num_classes=10)
y_test = to_categorical(y_test, num_classes=10)
y_train.shape,y_test.shape

#reshaping to shape required by CNN
x_train=np.reshape(x_train,(x_train.shape[0], 40,1,1))
x_test=np.reshape(x_test,(x_test.shape[0], 40,1,1))

#shapes
x_train.shape,x_test.shape

#forming model
model=Sequential()

#adding layers and forming the model
model.add(Conv2D(64,kernel_size=5,strides=1,padding="Same",activation="relu",input_shape=(40,1,1)))
model.add(MaxPooling2D(padding="same"))

model.add(Conv2D(128,kernel_size=5,strides=1,padding="same",activation="relu"))
model.add(MaxPooling2D(padding="same"))
model.add(Dropout(0.3))

model.add(Flatten())

model.add(Dense(256,activation="relu"))
model.add(Dropout(0.3))

model.add(Dense(512,activation="relu"))
model.add(Dropout(0.3))

model.add(Dense(10,activation="softmax"))

#compiling
model.compile(optimizer="adam",loss="categorical_crossentropy",metrics=["accuracy"])

#training the model
model.fit(x_train,y_train,batch_size=50,epochs=30,validation_data=(x_test,y_test))

#train and test loss and scores respectively
train_loss_score=model.evaluate(x_train,y_train)
test_loss_score=model.evaluate(x_test,y_test)
print(train_loss_score)
print(test_loss_score)

model.summary()

model.save('urbanClassifier')

converter = tf.lite.TFLiteConverter.from_keras_model(model)
tflite_model = converter.convert()

# Save the TF Lite model.
with tf.io.gfile.GFile('model.tflite', 'wb') as f:
  f.write(tflite_model)

x_test[0].shape

testData = np.expand_dims(x_test[5],axis=0)
prediction = model.predict_classes(testData)
prediction
y_test[5]

# Load the TFLite model and allocate tensors.
interpreter = tf.lite.Interpreter(model_path="model.tflite")
interpreter.allocate_tensors()

# Get input and output tensors.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

input_details

testData = np.expand_dims(x_test[5],axis=0)
atData = np.float32(testData)

input_shape = input_details[0]['shape']
input_data = np.array(np.random.random_sample(input_shape), dtype=np.float32)
interpreter.set_tensor(input_details[0]['index'], atData)
print(input_data.dtype)
print(testData.shape)
interpreter.invoke()

# The function `get_tensor()` returns a copy of the tensor data.
# Use `tensor()` in order to get a pointer to the tensor.
output_data = interpreter.get_tensor(output_details[0]['index'])
print(output_data)


# In[ ]:




