import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:frontend_scv/models/contract.dart';
import 'package:frontend_scv/screens/contract_detail_screen.dart';

class ContractListItem extends StatefulWidget {
  const ContractListItem({
    required Key key,
    required this.contract,
    // required this.deleteTx,
  }) : super(key: key); //set key in parent -

  final Contract contract;

  @override
  _ContractListItemState createState() => _ContractListItemState();
}

class _ContractListItemState extends State<ContractListItem> {
  void selectContract(BuildContext ctx, Contract selected) {
    Navigator.of(ctx)
        .pushNamed(
      ContractDetailScreen.routeName,
      arguments: selected,
    )
        .then(
      (result) {
        print(result);
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    Widget getContractStatus(Contract cont) {
      switch (cont.status) {
        case ContractStatus.Negotiation:
          return Row(
            children: [
              SizedBox(
                height: 5,
              ),
              Icon(
                Icons.dynamic_form,
                color: Colors.teal,
              ),
              SizedBox(
                width: 5,
              ),
              Text(
                'Negotiation Phase',
                style: TextStyle(color: Colors.teal),
              ),
            ],
          );
          break;
        case ContractStatus.Sealed:
          return Row(
            children: [
              SizedBox(
                height: 5,
              ),
              Icon(
                Icons.security,
                color: Colors.green,
              ),
              SizedBox(
                width: 5,
              ),
              Text(
                'Sealed',
                style: TextStyle(color: Colors.green),
              ),
            ],
          );
          break;

        default:
          return Text('Created');
      }
    }

    return Card(
      color: Colors.black38,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          ListTile(
            leading: Icon(
              Icons.work,
              color: Theme.of(context).accentColor,
            ),
            title: Text(
              widget.contract.id,
              style: TextStyle(
                  color: Theme.of(context).primaryColor,
                  fontSize: 20,
                  fontWeight: FontWeight.bold),
            ),
            subtitle: getContractStatus(widget.contract),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              TextButton.icon(
                icon: Icon(
                  Icons.visibility,
                  // color: Theme.of(context).accentColor,
                ),
                onPressed: () => selectContract(context, widget.contract),
                // onTap: () => selectMeal(context)
                label: Text('View Agreement'),
              ),
              const SizedBox(
                width: 10,
              ),
            ],
          ),
          const SizedBox(
            height: 10,
          ),
        ],
      ),
    );
  }
}
