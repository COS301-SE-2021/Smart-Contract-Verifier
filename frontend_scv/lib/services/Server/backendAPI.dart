//This class is used for interaction with the deployed server backend. It is also a singleton.
//String baseUrl is the root url of the server.
//When using these functions, the url passed in is effectively the api endpoint, an extension to the root url.

import 'dart:async';
import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart';
import 'package:retry/retry.dart';
import 'package:unison/services/Server/apiResponse.dart';

//Request Type
enum ReqType {
  GET, POST, PUT, DELETE
}

class ApiInteraction {
  final String baseUrl =
      "http://localhost:8080"; //Url where the backend is deployed
  static final ApiInteraction api =
      ApiInteraction._internal(); //Only instance of the class

  ///Factory constructor to return instance;
  factory ApiInteraction() {
    return api;
  }

  ApiInteraction._internal(); //Private constructor

  ///Get data from the server
  Future<ApiResponse> getData(String url) async {
    return _baseRequest(url, ReqType.GET);
  }

  ///Post data to the server
  Future<ApiResponse> postData(
      String url, Map<dynamic, dynamic> jsn) async {

    return _baseRequest(url, ReqType.POST, jsn);
  }

  ///Put data on the server
  Future<ApiResponse> putData(String url, [Map<dynamic, dynamic> jsn]) async {
    return _baseRequest(url, ReqType.PUT, jsn);
  }


    }
  }

