//This class models general statistics of Unison

class GeneralStats {

  int totalUsers;
  int numAgreements;
  int numJudges;
  int numSealed;
  int numUnsealed;
  double averageNegPeriod;
  int concluded;
  int disputed;

  ///This default constructor is mostly for testing
  GeneralStats() {
    totalUsers = 10;
    numAgreements = 20;
   // numJudges = 15;
    numSealed = 5;
    numUnsealed = 0;
    averageNegPeriod = 30.5;
    concluded = 3;
    disputed = 6;
  }

  GeneralStats.fromJSON(Map<String, dynamic> jsn) {
      totalUsers = jsn['TotalUsers'];
      numAgreements = jsn['TotalAgreements'];
      //numJudges = jsn['NumberOfJudges'];
      numSealed = jsn['SealedAgreements'];
      numUnsealed = jsn['UnSealedAgreements'];
      averageNegPeriod = jsn['AverageNegotiationPeriod'];
      concluded = jsn['ConcludedAgreements'];
      disputed = jsn['DisputedAgreements'];
  }
}