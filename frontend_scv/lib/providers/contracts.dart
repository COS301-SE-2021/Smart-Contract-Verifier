import 'package:flutter/material.dart';
import '../models/contract.dart';

class Contracts with ChangeNotifier {
  List<Contract> _contracts = [
    Contract(
      id: 'a967075d-d406-4cd6-b57f-8deb18940bf7',
      terms: [
        Term(
            '1',
            'term text',
            'term description - '
                'normally a bit longer than your average sentence to demonstrate what '
                'this will be used for.',
            TermStatus.Pending,
            '0x743Fb032c0bE976e1178d8157f911a9e825d9E23'),
      ],
      status: ContractStatus.Negotiation,
      partyA: '0x743Fb032c0bE976e1178d8157f911a9e825d9E23',
      partyB: '0x37Ec9a8aBFa094b24054422564e68B08aF3114B4',
      createdDate: "2021-06-19T23:47:05.454+00:00",
      movedToBlockchain: true,
      duration: '',
      sealedDate: '',
    ),
    Contract(
      id: '8deb18940bf7-4cd6-d406-b57f-a967075d',
      terms: [
        Term('0', 'Vehicle color', 'The car must be resprayed blue',
            TermStatus.Pending, '0x743Fb032c0bE976e1178d8157f911a9e825d9E23'),
        Term(
            '1',
            'Vehicle service',
            'Proof of a service from a qualified technician is required',
            TermStatus.Pending,
            '0x743Fb032c0bE976e1178d8157f911a9e825d9E23'),
      ],
      status: ContractStatus.Negotiation,
      partyA: '0x743Fb032c0bE976e1178d8157f911a9e825d9E23',
      partyB: '0x37Ec9a8aBFa094b24054422564e68B08aF3114B4',
      createdDate: "2021-06-20T23:47:05.454+00:00",
      movedToBlockchain: false,
      duration: '',
      sealedDate: '',
    ),
  ];

  List<Contract> get contracts {
    return [
      ..._contracts
    ]; //returns a copy of items - prevents direct access to
    // our actual list of items - if direct access was allowed, any listeners
    // would not be able to receive updates that new data is available,
    // because notifyListeners() would not be called...
  }

  Contract findById(String id) {
    return _contracts.firstWhere(
      (cont) => cont.id == id,
    );
  }

  addContract() {
    // _contracts.add(value);
    notifyListeners(); //Widgets 'listening' to this class will get rebuilt
  }
}
