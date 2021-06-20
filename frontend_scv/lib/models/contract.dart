import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

enum TermStatus { Accepted, Rejected, Pending }
enum ContractStatus {
  Negotiation,
  Sealed,
}

class Term {
  final String id;
  final String text;
  final String description;
  final TermStatus status;
  final String partyId;

  Term(this.id, this.text, this.description, this.status, this.partyId);
}

class Contract {
  final String id;
  final List<Term> terms;
  final ContractStatus status;
  final String partyA;
  final String partyB;
  final String createdDate;
  final bool movedToBlockchain;

  Contract({
    required this.id,
    required this.terms,
    required this.status,
    required this.partyA,
    required this.partyB,
    required this.createdDate,
    required this.movedToBlockchain,
  });
}
