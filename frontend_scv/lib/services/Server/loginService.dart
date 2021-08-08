//This class is for the initial login, and the flow that will come with it.

import '../../providers/global.dart';
import 'backendAPI.dart';

class LoginService {
  //TODO list:
  //Implement login flow when server is ready.
    // - Signing with Metamask etc.

  ApiInteraction api = ApiInteraction();

  Future<void> refreshToken() async { //Starts the login procedure to get a token to use in backend api requests

    Global.apiToken = ''; //Will eventually be used in all following requests to backend.

  }

}