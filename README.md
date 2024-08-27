# WiFi Aware LED blink

This repository contains two main projects that demonstrate the implementation of WiFi Aware using ESP32 and an Android application. Whole setup allows to control blinking led. The WiFi Aware technology allows devices to discover and connect to each other without the need for a traditional access point, enabling peer-to-peer communication.

## Project Structure
```
. ├── ESP32_FW 
  │ ├── main 
  │ ├── components 
  │ ├── sdkconfig 
  │ └── ... 
  └── Android_APP 
    ├── app 
    ├── build.gradle 
    ├── ...
```



### ESP32 Firmware (`ESP32_FW`)

The `ESP32_FW` directory contains the firmware project for the ESP32 microcontroller. This firmware handles the WiFi Aware communication on the ESP32 side.

### Android Application (`Android_APP`)

The `Android_APP` directory contains the Android application project that interfaces with the ESP32 via WiFi Aware. This app discovers, connects, and communicates with the ESP32 device.

## Requirements

### Hardware
- [ESP32](https://www.espressif.com/en/products/socs/esp32)
- Android Device with WiFi Aware support

### Software
- [Espressif ESP-IDF](https://docs.espressif.com/projects/esp-idf/en/latest/esp32/get-started/index.html) (for ESP32 development)
- Android Studio (for Android app development)

## Setup Instructions

### ESP32 Firmware

1. **Clone the repository**:
    ```bash
    git clone <repo-url>
    cd ESP32_FW
    ```

2. **Configure the ESP32 Project**:
    - Use `menuconfig` to configure the project:
    ```bash
    idf.py menuconfig
    ```
    - Set up WiFi, and other relevant configurations.

3. **Build and Flash the Firmware**:
    ```bash
    idf.py build
    idf.py flash
    ```

4. **Monitor the ESP32 Output**:
    ```bash
    idf.py monitor
    ```
![Zrzut ekranu (6)](https://github.com/user-attachments/assets/2b58fef8-9aad-4a5a-9a38-ad7ee12d6e7b)

### Android Application

1. **Open the Project**:
    - Open the `Android_APP` directory in Android Studio.

2. **Build the Project**:
    - Build the Android application using Android Studio.

3. **Run the App**:
    - Deploy the app on a device that supports WiFi Aware.

![Screenshot_20240827-204912](https://github.com/user-attachments/assets/baecd30e-f3e9-4ccb-a727-9aa2daffcc4c)

## Demonstration Video

A short video demonstrating the WiFi Aware communication between the ESP32 and the Android app can be found 

https://github.com/user-attachments/assets/3d35899d-22fb-47ce-bd04-eee489e7cc66



## Documentation

- **Espressif ESP32 Documentation**: [link_to_espressif_doc]
- **Android WiFi Aware Documentation**: [link_to_android_doc]

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
