//This file is the service used for interacting with the UnisonToken smart contract.

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

  Future<void> setAllowance(String add, BigInt amount) async {

    try {
      await _smC.makeWriteCall(
          'approve', [EthereumAddress.fromHex(add), amount]);
    }
    catch (e) {
      print (e);
      return;
    }

  }

  Future<dynamic> getAllowance() async {

    return (await _smC.makeReadCall('allowance', [EthereumAddress.fromHex(Global.userAddress), EthereumAddress.fromHex(await Global.getContractId('Verifier'))]))[0];

  }

}