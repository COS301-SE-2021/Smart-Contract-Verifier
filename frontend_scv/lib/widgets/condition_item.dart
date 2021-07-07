import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../providers/auth.dart';
import '../providers/contract.dart';

class ConditionItem extends StatelessWidget {
  final dynamic contractCondition;
  ConditionItem({
    @required this.contractCondition,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: Text(contractCondition.toString()),
      leading: CircleAvatar(
        backgroundColor: Colors.deepOrange,
        // backgroundImage: NetworkImage(contract.imageUrl),
      ),
      trailing: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          IconButton(
            icon: Icon(
              Icons.thumb_down_outlined,
              color: Colors.deepOrangeAccent,
            ),
            onPressed: null,
          ),
          IconButton(
            icon: Icon(
              Icons.thumb_up_outlined,
              color: Colors.cyan,
            ),
            onPressed: null,
          ),
        ],
      ),
    );
  }
}
