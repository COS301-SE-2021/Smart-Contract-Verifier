@JS()
library Web3;

import 'package:js/js.dart';

@JS()
external Future<String> signWithMetamask(String dat, String ad);

///Add an ethereum network if it is not yet tracked by Metamask
@JS()
external Future<void> addNetwork(String name, String url, String id, String cName, String cSymbol,);