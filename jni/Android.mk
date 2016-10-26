LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := SerialPort
LOCAL_SRC_FILES := SerialPort.c

include $(BUILD_SHARED_LIBRARY)
