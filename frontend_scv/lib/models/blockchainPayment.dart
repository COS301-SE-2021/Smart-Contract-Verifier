//This class describes payment information for an agreement on the blockchain.
//Though Verifier supports it, it is here assumed that only one
// payment condition exists for an agreement.

class BlockChainPayment {

  String payingUser;
  String payedUser;
  BigInt amount;
  bool hasBeenPayed;
  bool infoSet; //Is there payment information?

  BlockChainPayment.fromArray(List<dynamic> data) {

    //No payment set
    if (data.length == 0) {
      infoSet = false;
      return;
    }

    infoSet = true;

    //data[0] is the address of the coin
    amount = data[1];
    payingUser = data[2].toString();
    payedUser = data[3].toString();
    hasBeenPayed = data[4];
  }

}