//This file is the service used for interacting with the UnisonToken smart contract.

import 'dart:math';

import 'package:unison/models/global.dart';
import 'package:web3dart/credentials.dart';
import 'smartContract.dart';

class TokenService {

  SmartContract _smC = SmartContract("JSON/_src_UnisonToken_sol_UnisonToken.abi", 'UnisonToken');

  Future<void> setStake(BigInt stake) async { //Let the user stake an amount on UnisonToken.

    try {
      String id = await Global.getContractId('Verifier');
      print ('ID: ' + id);
     await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(id), stake]);
    }
    catch (e) {
      print (e);
      return;
    }

  }

  //Sets the Verifier allowance of UnisonToken
  Future<void> setAllowance(BigInt amount) async {

    try {
      await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(await Global.getContractId('Verifier')), amount]);
    }
    catch (e) {
      print (e);
      return;
    }

  }

  Future<BigInt> getAllowance() async {

    final res = await _smC.makeReadCall('allowance', [EthereumAddress.fromHex(Global.userAddress), EthereumAddress.fromHex(await Global.getContractId('Verifier'))]);
    if (res[0] == null)
      return BigInt.from(0);
    return res[0]/pow(10, 18); //Division to return in eth, not gwei

  }

}