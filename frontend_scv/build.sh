#!/bin/bash
flutter build web
cp assets/* build/web/assets/
docker build . -t frontend_scv