import 'package:flutter/material.dart';
import 'package:frontend_scv/screens/contract_detail_screen.dart';

class ContractItem extends StatelessWidget {
  final String id;
  final String status;

  ContractItem(this.id, this.status);

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(10),
      child: GridTile(
        child: GestureDetector(
            onTap: () {
              Navigator.of(context).pushNamed(
                ContractDetailScreen.routeName,
                arguments: id,
              );
            },
            child: Text(
              id,
              textAlign: TextAlign.center,
            )),
        footer: GridTileBar(
          backgroundColor: Colors.black87,
          leading: Text(status),
          trailing: IconButton(
            alignment: Alignment.centerRight,
            icon: Icon(Icons.favorite),
            onPressed: () => {},
            color: Theme.of(context).accentColor,
          ),

          // title: Text(),
        ),
      ),
    );
  }
}
// ;
