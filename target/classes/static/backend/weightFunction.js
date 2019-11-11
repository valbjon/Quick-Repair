
function calcWeights(range, price, reviews, days) {
    var weights = { 
        range: 1/(1-Math.exp(range)),
        price: 1/(1-Math.exp(price)),
        reviews: 1/(1-Math.exp(reviews)),
        days: 1/(1-Math.exp(days))
    };

    return (weights.range * 0.2) + (weights.price * 0.4) + (weights.reviews * 0.2) + (weights.days * 0.2);

}

// Virtual Server
// team6-backend
// Team6@Debeka


// team6-sql-server.database.windows.net
// team6AtDebeka
// HQ19AtDebeka