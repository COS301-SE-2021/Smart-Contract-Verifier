import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/auth.dart';
import '../providers/contract.dart';
import '../screens/view_contract_screen.dart';

class ContractItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final contract = Provider.of<Contract>(context);
    final authData = Provider.of<Auth>(context, listen: false);

    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewContractScreen.routeName,
          // ContractDetailScreen.routeName,
          arguments: contract.contractId,
        );
      },
      child: ListTile(
        title: Text(contract.title),
        leading: CircleAvatar(
          backgroundImage: NetworkImage(contract.imageUrl),
        ),
        trailing: Container(
          width: 50,
          child: Row(
            children: <Widget>[
              Consumer<Contract>(
                //consumer takes a builder:
                builder: (ctx, product, child) => IconButton(
                  icon: Icon(
                    product.isFavorite ? Icons.favorite : Icons.favorite_border,
                  ),
                  // label: child,
                  onPressed: () => {
                    // product.toggleFavoriteStatus(
                    //   authData.userWalletAddress,
                    // ),
                  },
                  color: Theme.of(context).accentColor,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
