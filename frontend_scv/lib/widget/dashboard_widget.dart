import 'package:flutter/material.dart';
// import 'package:frontend/widget/dashboard_card_widget.dart';

class Dashboard extends StatelessWidget {
  final dashDataList = [
    {'name': 'Contract A', 'percentage': 10},
    {'name': 'Contract B', 'percentage': 40},
    {'name': 'Contract C', 'percentage': 60},
    {'name': 'Contract D', 'percentage': 90},
  ];

  @override
  Widget build(BuildContext context) {
    var name;
    var percentage;

    return ListView(
      children: List.generate(
        dashDataList.length,
        (index) {
          name = dashDataList[index]['name'];
          percentage = dashDataList[index]['percentage'];
          // return CircularPercentageIndicator(percentage);
          return Text(
            "$name",
          );
          // return DashboardCard(
          //   contractName: name,
          //   percentage: percentage,
          // );
        },
      ),
    );
  }
}
