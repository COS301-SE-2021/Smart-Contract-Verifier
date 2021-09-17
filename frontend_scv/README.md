# frontend_scv

Flutter module which provides an interface for the blockchain and backend modules of the smart contract verifier system

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.


HOW TO DOCKER

To build the docker image, run the following command:  docker build . -t frontend_scv

To run the docker image, run the following command: docker run -i -p 8080:4040 -td frontend_scv
    In the above command, 4040 is the port within the docker image, while 8080 is the exposed port
    so if you want to host on a different port, replace the 808
