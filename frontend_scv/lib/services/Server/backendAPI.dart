//This class is used for interaction with the deployed server backend. It is also a singleton.
//String baseUrl is the root url of the server.
//When using these functions, the url passed in is effectively the api endpoint, an extension to the root url.

import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart';
import 'package:retry/retry.dart';

class ApiInteraction {
  final String baseUrl =
      "http://localhost:8080"; //Url where the backend is deployed
  static final ApiInteraction api =
      ApiInteraction._internal(); //Only instance of the class

  factory ApiInteraction() {
    //Factory constructor to return instance;
    return api;
  }

  ApiInteraction._internal(); //Private constructor

  Future<Map<dynamic, dynamic>> getData(String url) async {
    //Pass in url extension
    var response;
    try {
      response = await RetryOptions(maxAttempts: 5).retry(
        () => get(Uri.parse(baseUrl + url)).timeout(Duration(seconds: 5)),
        retryIf: (e) => e is SocketException || e is TimeoutException,
      );
    } on Exception catch (e) {
      //Some other exception
      print(e);
      throw Exception(
          'Could not connect to backend'); //Could be expanded in the future
    }

    if (response.statusCode != 200)
      throw Exception('Request could not be made'); //Failed http request

    return json.decode(response.body);
  }

  Future<Map<String, dynamic>> postData(
      String url, Map<dynamic, dynamic> jsn) async {
    //Pass in url extension and json body
    var response;

    try {
      response = await RetryOptions(maxAttempts: 5).retry(
        () => post(Uri.parse(baseUrl + url),
                headers: <String, String>{
                  'Content-Type': 'application/json; charset=UTF-8',
                },
                body: jsonEncode(jsn))
            .timeout(Duration(seconds: 1)),
        retryIf: (e) => e is SocketException || e is TimeoutException,
      );
      // print (response.body.toString());

    } on Exception catch (e) {
      print('Error: ' + e.toString());
      throw Exception(
          'Could not connect to backend'); //Could be expanded in the future
    }

    if (response.statusCode != 200)
      throw Exception(
          'An error occurred while making the request. The server responded with status code ' +
              response.statusCode.toString()); //Failed http request

    return json.decode(response.body);
  }

  Future< /*Map<String, dynamic>*/ void> putData(String url) async {
    var response;
    try {
      response = await RetryOptions(maxAttempts: 5).retry(
        () => put(
          Uri.parse(baseUrl + url),
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
        ).timeout(Duration(seconds: 1)),
        retryIf: (e) => e is SocketException || e is TimeoutException,
      );
      // print(response.body.toString());
    } on Exception catch (e) {
      print('Error: ' + e.toString());
      throw Exception(
          'Could not connect to backend'); //Could be expanded in the future
    }

    if (response.statusCode != 200)
      throw Exception(
          'An error occurred while making the request. The server responded with status code ' +
              response.statusCode.toString()); //Failed http request
  }
}
