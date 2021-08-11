import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/commonService.dart';
import 'contract.dart';
import 'http_exception.dart';

class Contracts with ChangeNotifier {
  List<Contract> _items = [];
  CommonService commonService = CommonService();

  final String userWalletAddress;
  Contracts(
    this.userWalletAddress,
    this._items,
  );

  List<Contract> get items {
    return [..._items];
  }

  Contract findById(String id) {
    return _items.firstWhere(
      (cont) => cont.contractId == id,
    );
  }

  //Add a getter to fetch only favourites:
  List<Contract> get favoriteItems {
    return _items.where((contItem) => contItem.isFavorite).toList();
  }

  Future<void> fetchAndSetContracts() async {
    try {
      //Using Common Service
      print(Global.userAddress);
      final List<Contract> loadedContracts =
          await commonService.getInvolvedAgreements(Global.userAddress);
      _items = loadedContracts;
      notifyListeners();
    } catch (error) {
      throw (error);
    }
  }

  Future<void> addContract(Contract contract) async {
    final url = //RC: Does this correspond to the GetAgreement call of the api? The fields are different
        'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/contracts'
        '.json';
    try {
      final response = await http //wait for this to finish
          .post(
        Uri.parse(url),
        body: json.encode({
          'title': contract.title,
          'description': contract.description,
          'imageUrl': contract.imageUrl,
          'price': contract.price,
          'partyBId': contract.partyBId,
          'creatorId': userWalletAddress,
          'conditions': contract.conditions,
        }),
      );

      //RC: I believe this can be replaced with a fromJson constructor
      //The code below will only run after the await is complete
      final newContract = Contract(
        title: contract.title,
        description: contract.description,
        price: contract.price,
        partyBId: contract.partyBId,
        imageUrl: contract.imageUrl,
        contractId: json.decode(response.body)['name'],
        conditions: contract.conditions,
      );
      _items.add(newContract);
      // _items.insert(0, newContract); // at the start of the list
      notifyListeners();
    } catch (error) {
      print(error);
      throw error;
    }
  }

  Future<void> updateContract(String id, Contract newContract) async {
    final contIndex = _items.indexWhere((cont) => cont.contractId == id);
    if (contIndex >= 0) {
      //Send patch request
      final url = 'https://capstone-testing-a7ee4-default-rtdb.firebaseio'
          '.com/contracts/$id.json';
      //can do try catch here:
      await http.patch(Uri.parse(url),
          body: json.encode({
            'title': newContract.title,
            'description': newContract.description,
            'imageUrl': newContract.imageUrl,
            'price': newContract.price,
            'partyBId': newContract.partyBId,
            'conditions': newContract.conditions,
          }));
      _items[contIndex] = newContract;
      notifyListeners();
    } else {
      print('...');
    }
  }

  Future<void> deleteContract(String id) async {
    final url = 'https://capstone-testing-a7ee4-default-rtdb.firebaseio'
        '.com/contracts/$id.json';
    final existingContractIndex =
        _items.indexWhere((cont) => cont.contractId == id);
    var existingContract = _items[existingContractIndex];
    _items.removeAt(existingContractIndex);
    notifyListeners();
    final response = await http.delete(Uri.parse(url));
    if (response.statusCode >= 400) {
      _items.insert(existingContractIndex, existingContract);
      notifyListeners();
      throw HttpException('Could not delete contract.');
    }
    existingContract = null;
  }
}
