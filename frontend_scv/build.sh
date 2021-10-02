#!/bin/bash
flutter build web
cp assets/* build/web/assets/ -r
docker build . -t frontend_scv