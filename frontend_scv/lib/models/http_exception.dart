class HttpException implements Exception {
  //implements forces use to implement all functions that the parent has
  final String message;

  HttpException(this.message);

  @override
  String toString() {
    return message;
    // return super.toString();
  }
}
