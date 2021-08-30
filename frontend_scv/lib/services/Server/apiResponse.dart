//This class is a container for api responses using the new api endpoints

class ApiResponse {

  bool successful;
  Map<String, dynamic> result = {}; //The result of the call, if any
  String errorMessage = '';

  ApiResponse.fromJSON(Map<String, dynamic> jsn) {
    successful = jsn['Status'] == 'SUCCESSFUL';

    if (successful) {
      try { //Add response data if there is any
        result = jsn['ResponseData'];
      } catch (_) {}
    }
    else {
      errorMessage = jsn['Message'];
    }
  }

  ApiResponse.fromError(String err) { //Generate an response indicating an error without fromJSON.
    successful = false;
    errorMessage = err;
  }

}