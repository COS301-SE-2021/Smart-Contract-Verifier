import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import './contract.dart';
import '../models/http_exception.dart';

class Contracts with ChangeNotifier {
  List<Contract> _items = [];
  final String authToken;
  final String userId;
  Contracts(
    this.authToken,
    this.userId,
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

  Future<void> fetchAndSetContracts([bool filterByUser = false]) async {
    final filterString =
        filterByUser ? 'orderBy="creatorId"&equalTo="$userId"' : '';
    var url = 'https://capstone-testing-a7ee4-default-rtdb.firebaseio'
        '.com/contracts.json?auth=$authToken&$filterString';
    try {
      final response = await http.get(url);
      // print(json.decode(response.body));
      final extractedData = json.decode(response.body) as Map<String, dynamic>;

      if (extractedData == null) {
        return;
      }

      url =
          'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/userFavorites/$userId.json?auth=$authToken';
      final favoriteResponse = await http.get(url);
      final favoriteData = json.decode(favoriteResponse.body);
      final List<Contract> loadedContracts = [];
      extractedData.forEach((contId, contData) {
        loadedContracts.add(Contract(
          contractId: contId,
          title: contData['title'],
          description: contData['description'],
          price: contData['price'],
          partyBId: contData['partyBId'],
          isFavorite:
              favoriteData == null ? false : favoriteData[contId] ?? false,
          imageUrl: contData['imageUrl'],
          conditions: contData['conditions'],
        ));
      });
      _items = loadedContracts;
      notifyListeners();
    } catch (error) {
      throw (error);
    }
  }

  Future<void> addContract(Contract contract) async {
    final url =
        'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/contracts'
        '.json?auth=$authToken';
    try {
      final response = await http //wait for this to finish
          .post(
        url,
        body: json.encode({
          'title': contract.title,
          'description': contract.description,
          'imageUrl': contract.imageUrl,
          'price': contract.price,
          'partyBId': contract.partyBId,
          'creatorId': userId,
          'conditions': contract.conditions,
        }),
      );
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
          '.com/contracts/$id.json?auth=$authToken';
      //can do try catch here:
      await http.patch(url,
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
        '.com/contracts/$id.json?auth=$authToken';
    final existingContractIndex =
        _items.indexWhere((cont) => cont.contractId == id);
    var existingContract = _items[existingContractIndex];
    _items.removeAt(existingContractIndex);
    notifyListeners();
    final response = await http.delete(url);
    if (response.statusCode >= 400) {
      _items.insert(existingContractIndex, existingContract);
      notifyListeners();
      throw HttpException('Could not delete contract.');
    }
    existingContract = null;
  }
}
