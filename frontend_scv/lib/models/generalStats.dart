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
  GeneralStats.fromJSON(Map<String, dynamic> jsn) {
      totalUsers = jsn['TotalUsers'];
      numAgreements = jsn['TotalAgreements'];
      numJudges = jsn['NumberOfJudges'];
      numSealed = jsn['SealedAgreements'];
      numUnsealed = jsn['UnSealedAgreements'];
      averageNegPeriod = jsn['AverageNegotiationPeriod'];
      concluded = jsn['ConcludedAgreements'];
      disputed = jsn['DisputedAgreements'];
  }
}