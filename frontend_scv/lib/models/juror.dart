//This class will be used to model a single juror (judge)

class Juror {

  String address;
  String vote;

  Juror(this.address, int b) {

    switch(b) {
      case 0: vote = 'Not voted';
            break;
      case 1: vote = 'No';
            break;
      case 2: vote = 'Yes';
            break;
    }
  }

}