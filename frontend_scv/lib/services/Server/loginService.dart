//This class is for the initial login, and the flow that will come with it.

import '../../models/global.dart';
import 'backendAPI.dart';

class LoginService {
  //TODO list:
  //Implement login flow when server is ready.
    // - Signing with Metamask etc.

  ApiInteraction _api = ApiInteraction();

  Future<void> refreshToken() async { //Starts the login procedure to get a token to use in backend api requests

    Global.apiToken = ''; //Will eventually be used in all following requests to backend.

  }

  Future<void> tryAddUser() async { //This attempts to add a new user to the DB. Failure means the user already exists

    try {
      await _api.postData('/add-user', {'WalletID': Global.userAddress, 'Alias' : 'Nothing'});

    } on Exception catch (e) {
      print(e);
      throw(e);
    }

  }

}