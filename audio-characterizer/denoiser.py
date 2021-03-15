#!/usr/bin/env python
# coding: utf-8

# In[8]:


import IPython
from scipy.io import wavfile
import noisereduce as nr
import soundfile as sf
from noisereduce.generate_noise import band_limited_noise
import matplotlib.pyplot as plt
import urllib.request
import numpy as np
import io
import os

import noisereduce as nr
from scipy.io import wavfile

from pydub import AudioSegment


for filename in os.listdir("test"):
    if filename.endswith(".wav"):
        # Turn the sound into mono
        sound = AudioSegment.from_wav(filename)
        sound = sound.set_channels(1)
        sound.export(filename, format="wav")

        #load data 
        rate, data = wavfile.read(filename) 
        data = data / 1.0
        print(len(data))
        # select section of data that is noise 
        noisy_part = data[0:len(data)] 
        # perform noise reduction 
        reduced_noise = nr.reduce_noise(audio_clip=data,noise_clip=noisy_part, verbose=True)

        #load the new version into a file
        wavfile.write("out_" + filename, rate, reduced_noise)
        continue
    else:
        continue


# In[ ]:




