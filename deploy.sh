#!/bin/bash
echo "deploy.sh started"
cd ./SmartContract
truffle migrate
cd ../frontend_scv
flutter pub get
echo "deploy.sh finished"