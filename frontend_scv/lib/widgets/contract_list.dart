// import 'package:flutter/foundation.dart';
// import 'package:flutter/material.dart';
// import 'package:frontend_scv/models/contract.dart';
// import 'package:frontend_scv/widgets/contract_list_item.dart';
//
// class ContractList extends StatefulWidget {
//   final List<Contract> contracts;
//   ContractList(this.contracts);
//   @override
//   _ContractListState createState() => _ContractListState();
// }
//
// class _ContractListState extends State<ContractList> {
//   @override
//   Widget build(BuildContext context) {
//     return widget.contracts.isEmpty
//         ? LayoutBuilder(builder: (ctx, constraints) {
//             return Column(
//               children: <Widget>[
//                 SizedBox(
//                   height: 20,
//                 ),
//                 Container(
//                   height: constraints.maxHeight * 0.40,
//                   child: Image.asset(
//                     'assets/images/noContractsDino.png',
//                     fit: BoxFit.cover,
//                     color: Theme.of(context).shadowColor,
//                   ),
//                 ),
//                 Text(
//                   'You have no Agreements!',
//                   style: Theme.of(context).textTheme.body1,
//                 ),
//               ],
//             );
//           })
//         : kIsWeb
//             ? Container(
//                 padding: EdgeInsets.symmetric(vertical: 50, horizontal: 500),
//                 child: buildContractItemsList(),
//               )
//             : Container(
//                 margin: EdgeInsets.symmetric(
//                   vertical: 30,
//                   horizontal: 20,
//                 ),
//                 child: buildContractItemsList(),
//               );
//   }
//
//   ListView buildContractItemsList() {
//     return ListView(
//       children: widget.contracts
//           .map((cont) => ContractListItem(
//                 key: ValueKey(cont.id), //You pass your own identifier
//                 // key: UniqueKey(),
//                 contract: cont,
//                 // deleteTx: deleteTx,
//               ))
//           .toList(),
//     );
//   }
// }
