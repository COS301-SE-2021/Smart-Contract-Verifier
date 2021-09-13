#!/bin/bash
echo "deploy.sh started"
cd ./SmartContract
npx truffle migrate
cd ../frontend_scv
flutter pub get
echo "deploy.sh finished"