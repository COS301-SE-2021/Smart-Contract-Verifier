//This file is the service used for interacting with the UnisonToken smart contract.

import 'package:unison/models/global.dart';

import 'smartContract.dart';

class TokenService {
  SmartContract _smC =
      SmartContract("JSON/_src_UnisonToken_sol_UnisonToken.abi", 'UnisonToken');

  Future<void> setStake() async {
    //Let the user stake an amount on UnisonToken

    try {
      await _smC.makeWriteCall(
          'approve', [Global.userAddress, 10000]); //User stakes 10 000 gwei
    } catch (e) {
      print(e);
      return;
    }
  }
}
