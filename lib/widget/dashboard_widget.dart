import 'package:flutter/material.dart';

class Dashboard extends StatelessWidget {
  final dashDataList = [
    {'name': 'Contract A','percentage':10},
    {'name': 'Contract B','percentage':40},
    {'name': 'Contract C','percentage':60},
  ];

  @override
  Widget build(BuildContext context) {
    var name;
    var percentage;
    return GridView.count(
      crossAxisCount: 1,
      children: List.generate(
        dashDataList.length,
        (index) {
          name = dashDataList[index]['name'];
          percentage = dashDataList[index]['percentage'];
          return Center(
            child: Text(
              "$name $percentage",
              style: Theme.of(context).textTheme.headline5,
            ),
          );
        },
      ),
    );
  }
}
