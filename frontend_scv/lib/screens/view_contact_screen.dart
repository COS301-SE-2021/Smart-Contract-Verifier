// //Not to be confused with view contract screen
//
// import 'package:flutter/material.dart';
// import 'package:provider/provider.dart';
// import 'package:unison/models/condition.dart';
// import 'package:unison/models/contact.dart';
// import 'package:unison/models/global.dart';
// import 'package:unison/screens/messaging_screen.dart';
// import 'package:unison/services/Server/negotiationService.dart';
//
// import '../models/contracts.dart';
// import '../widgets/contract_conditions_panel.dart';
// import '../widgets/contract_detail_info_panel.dart';
//
// class ViewContactScreen extends StatefulWidget {
//   static const routeName = '/view-contact';
//
//   @override
//   _ViewContactScreenState createState() => _ViewContactScreenState();
// }
//
// enum ConditionType { Normal, Payment, Duration }
//
// class _ViewContactScreenState extends State<ViewContactScreen> {
//   final _conditionTitleController = TextEditingController();
//   final _conditionDescriptionController = TextEditingController();
//   final _paymentConditionAmountController = TextEditingController();
//   final _durationConditionAmountController = TextEditingController();
//   var _isLoading = false;
//   // var _isInit = true;
//
//   NegotiationService negotiationService = NegotiationService();
//
//   GlobalKey<FormState> _formKey = GlobalKey<FormState>();
//
//   @override
//   void initState() {
//     // print('InitState()');
//     super.initState();
//   }
//
//
//
//   bool isNumeric(String s) {
//     if (s == null) {
//       return false;
//     }
//     return double.tryParse(s) != null;
//   }
//
//
//
//   Widget build(BuildContext context) {
//     final contactId = ModalRoute.of(context).settings.arguments as String;
//     final loadedContact =
//     Provider.of<Contracts>(context, listen: true).findById(contactId);
//
//     return Scaffold(
//       appBar: AppBar(
//         title: Text(loadedContact.alias),
//       ),
//       body: Padding(
//         padding: const EdgeInsets.all(15.0),
//         child: Column(
//           children: [
//             ContractDetailInfoPanel(loadedContract),
//             Container(
//               padding: EdgeInsets.symmetric(
//                 vertical: 10,
//                 horizontal: 8,
//               ),
//               // alignment: Alignment.centerLeft,
//               child: Row(
//                 mainAxisAlignment: MainAxisAlignment.spaceBetween,
//                 children: [
//                   Text(
//                     'Agreement Conditions:',
//                     style: TextStyle(
//                       fontSize: 16,
//                     ),
//                   ),
//                   if (!loadedContract.movedToBlockchain)
//                     TextButton(
//                       onPressed: () async {
//                         await _newConditionDialog(contractId);
//                       },
//                       child: Row(
//                         children: [
//                           Icon(Icons.add),
//                           Text('Add New Condition'),
//                         ],
//                       ),
//                     ),
//                   if (!loadedContract.movedToBlockchain)
//                     TextButton(
//                       onPressed: () async {
//                         await _newSpecialConditionDialog(
//                             contractId, ConditionType.Payment);
//                       },
//                       child: Row(
//                         children: [
//                           Icon(Icons.add),
//                           Text('Add New Payment Condition'),
//                         ],
//                       ),
//                     ),
//                   if (!loadedContract.movedToBlockchain)
//                     TextButton(
//                       onPressed: () async {
//                         await _newSpecialConditionDialog(
//                             contractId, ConditionType.Duration);
//                       },
//                       child: Row(
//                         children: [
//                           Icon(Icons.add),
//                           Text('Add New Duration Condition'),
//                         ],
//                       ),
//                     ),
//                 ],
//               ),
//             ),
//             Expanded(
//               child: _isLoading
//                   ? Center(
//                 child: CircularProgressIndicator(),
//               )
//                   : ContractConditionsPanel(loadedContract),
//               flex: 6,
//             ),
//             Expanded(
//               flex: 1,
//               child: Container(
//                 // TODO: More Information or actions here?
//               ),
//             )
//           ],
//         ),
//       ),
//       floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
//       floatingActionButton: FloatingActionButton.extended(
//         onPressed: () {
//           Navigator.of(context).pushNamed(
//             MessagingScreen.routeName,
//             arguments: loadedContract.contractId,
//           );
//         },
//         label: Text('Agreement Chat'),
//         icon: Icon(Icons.chat),
//         backgroundColor: Color.fromRGBO(182, 80, 158, 1),
//       ),
//     );
//   }
// }
