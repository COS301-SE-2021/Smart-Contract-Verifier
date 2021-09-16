//This class interacts with the jurorStore smart contract. It is named jurorService to avoid confusion with the JudgeService,
//Though they handle the same concept.

//Note: This class has been made redundant.

import 'package:unison/models/global.dart';
import 'package:unison/services/Blockchain/smartContract.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:web3dart/credentials.dart';
import 'tokenService.dart';

class JurorService {

  TokenService _tokServ = TokenService();
  UnisonService _uniServ = UnisonService();
  //SmartContract _smC = SmartContract('JSON/_src_JurorStore_sol_JurorStore.abi', 'JurorStore');

  Future<void> addUserAsJuror() async { //Add the user to the jury

    await _tokServ.setStake(BigInt.from(10000)); //User stakes 10 000 gwei
    await _uniServ.addJuror();
  }

  Future<void> removeUserAsJuror() async {
      await _uniServ.removeJuror();
  }

  Future<void> vote(int v) async {
    //await _uniServ.jurorVote(v);
  }

  //Future<bool>
}