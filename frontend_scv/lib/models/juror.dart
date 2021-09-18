//This class will be used to model a single juror (judge)

class Juror {

  String address;
  String vote;
  int voteNumber;

  Juror(this.address, BigInt b) {

    switch(b.toInt()) {
      case 0: vote = 'Not voted';
            break;
      case 1: vote = 'No';
            break;
      case 2: vote = 'Yes';
            break;
      default: vote = 'Unavailable';

    }
    voteNumber = b.toInt();
  }

}