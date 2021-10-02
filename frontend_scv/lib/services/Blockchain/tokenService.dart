//This file is the service used for interacting with the UnisonToken smart contract.

import 'dart:math';
import 'package:unison/models/global.dart';
import 'package:web3dart/credentials.dart';
import 'smartContract.dart';

class TokenService {

  SmartContract _smC = SmartContract("JSON/_src_UnisonToken_sol_UnisonToken.abi", 'UnisonToken');

  ///Set the allowance that Verifier can use of UnisonToken
  Future<void> setStake(BigInt stake) async { //Let the user stake an amount on UnisonToken.

    try {
      String id = await Global.getContractId('Verifier');
     var res = await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(id), stake]);
    }
    catch (e) {
      print (e);
      return;
    }

  }

  ///Sets the Verifier allowance of UnisonToken
  Future<void> setAllowance(BigInt amount) async {

    try {
      await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(await Global.getContractId('Verifier')), amount]);
    }
    catch (e) {
      print (e);
      throw 'Could not update allowance.';
    }

  }

  ///Get the allowance that Verifier has of UnisonToken
  Future<double> getAllowance() async {

    final res = await _smC.makeReadCall('allowance', [EthereumAddress.fromHex(Global.userAddress), EthereumAddress.fromHex(await Global.getContractId('Verifier'))]);

      if (res[0] == null) {
        return 0;
      }

    double result = (res[0].toInt()/pow(10, 18)); //Division to return in eth, not gwei
    return result;

  }

  ///Get the UNT balance of the current user
  Future<double> getBalance() async {
    final res =  await _smC.makeReadCall('balanceOf', [EthereumAddress.fromHex(Global.userAddress)]);
    double balance =  res[0].toDouble()/pow(10, 18);
    return balance;
  }


}