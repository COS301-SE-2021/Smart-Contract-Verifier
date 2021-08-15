import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/screens/view_assignment_screen.dart';
import 'package:unison/screens/view_contract_screen.dart';

// import '../providers/auth.dart';
import '../models/contract.dart';
// TODO: import '../screens/view_case_contract_screen.dart';

class JudgeActiveContractItem extends StatelessWidget {
  final Contract agreement;

  JudgeActiveContractItem(this.agreement);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewAssignmentScreen.routeName,
          arguments: agreement.contractId,
        );
      },
      child: ListTile(
        title: Text(agreement.title),
        leading: CircleAvatar(
            // backgroundImage: NetworkImage(contract.imageUrl),
            ),
        trailing: Container(
          width: 50,
          child: Row(
            children: <Widget>[
              // Consumer<Contract>(
              //   //consumer takes a builder:
              //   builder: (ctx, product, child) => IconButton(
              //     icon: Icon(
              //       product.isFavorite ? Icons.favorite : Icons.favorite_border,
              //     ),
              //     // label: child,
              //     onPressed: () => {
              //       // product.toggleFavoriteStatus(
              //       //   authData.userWalletAddress,
              //       // ),
              //     },
              //     color: Theme.of(context).accentColor,
              //   ),
              // ),
            ],
          ),
        ),
      ),
    );
  }
}
