import 'dart:convert';
import 'package:retry/retry.dart';
import 'package:http/http.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:io';

class apiInteraction {

  final String baseUrl = "https://localhost:8080/"; //Url where the backend is deployed

  Future<Map<dynamic, dynamic>> get(String url) async{ //Pass in url extension

    final response = await RetryOptions(maxAttempts: 5).retry(
          () => get(baseUrl + url).timeout(Duration(seconds: 5)),
      retryIf: (e) => e is SocketException || e is TimeoutException,
    );

    return response;
  }

  Future<Map<dynamic, dynamic>> post(String url, Map json) async{ //Pass in url extension and json body

    final response = await RetryOptions(maxAttempts: 5).retry(
          () => post(baseUrl + url, json).timeout(Duration(seconds: 5)),
      retryIf: (e) => e is SocketException || e is TimeoutException,
    );

    return response;
  }

}