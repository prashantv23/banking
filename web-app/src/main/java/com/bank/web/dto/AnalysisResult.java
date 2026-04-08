package com.bank.web.dto;

import java.util.List;

public record AnalysisResult(String email, List<LoanOffer> offers) {
}
