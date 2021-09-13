//This class is used for interaction with the deployed server backend. It is also a singleton.
//String baseUrl is the root url of the server.
//When using these functions, the url passed in is effectively the api endpoint, an extension to the root url.

import 'dart:async';
import 'dart:convert';
import 'dart:io';
//import 'package:dio/dio.dart';
import 'package:file_picker/file_picker.dart';
import 'package:http/http.dart';// hide MultipartFile;
import 'package:retry/retry.dart';
import 'package:unison/services/Server/apiResponse.dart';

//Request Type
enum ReqType {
  GET, POST, PUT, DELETE
}

class ApiInteraction {
  final String _baseUrl =
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

  ///Delete data from the server
  Future<ApiResponse> deleteData(String url, [Map<dynamic, dynamic> jsn]) async {
    return _baseRequest(url, ReqType.DELETE, jsn);
  }

  //An attempt at re-organising the services.
  Future<ApiResponse> _baseRequest(String url, ReqType method, [Map<dynamic, dynamic> jsn]) async {
    jsn ??= {}; //If null, make empty

    Function toCall = get;
    //Cool function pointers
    switch (method) {
      case ReqType.GET : {toCall = get;
      break;}
      case ReqType.POST : {toCall = post;
      break;}
      case ReqType.PUT : {toCall = put;
      break;}
      case ReqType.DELETE : {toCall = delete;
      break; }
    }

    print ('Here 1');
    var headers = {
      'Content-Type': 'application/json; charset=UTF-8',
    };
    var response;
    try {
      response = await RetryOptions(maxAttempts: 5).retry(
            () => (method == ReqType.GET) ? toCall(Uri.parse(_baseUrl + url),
            headers: headers) :toCall(Uri.parse(_baseUrl + url),
          headers: headers, body: jsonEncode(jsn),)
            .timeout(Duration(seconds: 2)),
        retryIf: (e) => e is SocketException || e is TimeoutException,
      );

    } on Exception catch (e) {
      return ApiResponse.fromError(
          'Could not connect to backend'); //Could be expanded in the future
    }

    if (response.statusCode != 200)
      return ApiResponse.fromError(
          'An error occurred while making the request. The server responded with status code ' +
              response.statusCode.toString()); //Failed http request

    //print (response.body);
    return ApiResponse.fromJSON(jsonDecode(response.body));
  }

  Future<ApiResponse> filePost(String url, MultipartFile file) async {

    var req = await MultipartRequest('POST', Uri.parse(_baseUrl + url));
    req.files.add(
      file
    );

    String body;
    try {
      final res = await req.send();
      Response r = await Response.fromStream(res);
      print ("RES: " + r.body);
      body = r.body;
    }
    catch (e) {
      return ApiResponse.fromError(e);
    }

    return ApiResponse.fromJSON(jsonDecode(body));

  }

  Future<PlatformFile> fileGet(String url) async {

    var headers = {
      'Content-Type': 'application/json; charset=UTF-8',
    };

    var response = await RetryOptions(maxAttempts: 5).retry(
          () => get(Uri.parse(_baseUrl + url),
        headers: headers,)
          .timeout(Duration(seconds: 3)),
      retryIf: (e) => e is SocketException || e is TimeoutException,
    );

    return PlatformFile(name: 'Test', bytes: response.bodyBytes, size: response.bodyBytes.lengthInBytes);

  }
}