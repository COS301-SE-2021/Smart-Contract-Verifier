//These methods are all related to statistics concerning Unison.

import 'package:unison/models/generalStats.dart';
import 'package:unison/services/Server/apiResponse.dart';
import 'package:unison/services/Server/backendAPI.dart';

class StatsService {
  ApiInteraction _api = ApiInteraction();

  ///Get general statistics on Unison
  Future<GeneralStats> getGeneralStats() async {
    //Temporarily commented out

    ApiResponse res = await _api.getData('/stats');

    if (!res.successful) {
      throw "Could not retrieve statistics data";
    }

    return GeneralStats.fromJSON(res.result);

  }
}