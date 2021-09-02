//This is the UI counterpart of Contact from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/models/contact.dart';

import '../models/contract.dart';
import '../providers/auth.dart';
import '../screens/view_contract_screen.dart';

//Be aware of confusion with Contract (with an r in the middle)
class ContactItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final contact = Provider.of<Contact>(context);
    final String rawSvg = Jdenticon.toSvg(
      contact.address,
      colorSaturation: 1.0,
      grayscaleSaturation: 1.0,
      colorLightnessMinValue: 0.40,
      colorLightnessMaxValue: 0.80,
      grayscaleLightnessMinValue: 0.30,
      grayscaleLightnessMaxValue: 0.90,
      backColor: '#ff7800',
      hues: [205],
    );

    return ListTile(
      title: Text(contact.alias),
      subtitle: Text(contact.address),
      leading:
      SvgPicture.string(
        rawSvg,
        fit: BoxFit.fill,
        height: 32,
        width: 32,
      ),
    );

    // return GestureDetector(
    //   onTap: () {
    //     Navigator.of(context).pushNamed(
    //       ViewContractScreen.routeName,
    //       arguments: contract.contractId,
    //     );
    //   },
    //   child: ListTile(
    //     title: Text(contract.title),
    //     leading:
    //     // CircleAvatar(
    //     //   backgroundImage: SvgPicture.
    //     SvgPicture.string(
    //       rawSvg,
    //       fit: BoxFit.fill,
    //       height: 32,
    //       width: 32,
    //     ),
    //   ),
    // );
  }
}
