
// Oppgave 1:

function a() {
  // get the country code
  const countryCode: string = getCountryCode('REMOTE_ADDR');

  // if country code is US
  if (countryCode == 'US') {
    // display the form input for state
    console.log(formInputState());
  }
}

// Issues
// - Kommentarer som ikke gir mer verdi til forståelsen
// - Dårlig navngivning av funksjon
// - Magiske strings i koden
const CountryCodeAmerica = 'US';

function countryCodeIsAmerican() {
  const countryCode: string = getCountryCode('REMOTE_ADDR');

  if (countryCode == CountryCodeAmerica) {
    console.log(formInputState());
  }
}

// ---------------------------
// ---------------------------
// Oppgave 2:

function canBuyBeer(age, money) {
  if(age >= 21 && money >= 20) {
    return true
  }
  return false
}

// Issues
// - magical numbers
// - Mangler navn på boolean verdier.

// Her er koden kanskje ikke nødvendigvis så vanskelig å lese. Men hva betyr egentlig 21 og 20? Her må man analysere
// konteksten av hvilken variabel som brukes mot hvilet tall for å skjønne hva tallet betyr.
// Det samme hvis man senere ønsker å endre verdiene. Dette kan gi økt risiko for bugs.

function canBuyBeer(age, money) {
  const legalDrinkingAge = 21
  const beerPrice = 20

  const isOverLegalDrinkingAge = age >= legalDrinkingAge;
  const hasMoneyForBeer = money >= beerPrice;

  return isOverLegalDrinkingAge && hasMoneyForBeer;
}

// --------------------------
// --------------------------

// Oppgave 3:

function shouldShowImage(itemIndex, article, showAllImages) {
  return [0, 1, 2].includes(itemIndex)
    ? article.imageUrl != null
    : showAllImages
      ? article.imageUrl != null
      : false
}

// Issues

// - Too clever: Nestede ternaries gjør det vanskeligere å skjønne hva funksjonen faktisk gjør.
// - magical values: Hva beskriver egentlig 0,1,2? Her er det vanskelig å forstå utifra konteksten vi har, men imageIndexToShow
// gir litt mer.

// Refactor: 
// 1. Vi kan begynne med å flytte utrykkene til enklere if sjekker.

function shouldShowImage(itemIndex, article, showAllImages) {
  if ([0, 1, 2].includes(itemIndex)) {
    return article.imageUrl != null;
  } else if (showAllImages) {
    return article.imageUrl != null;
  } else {
    return false;
  }
}

// 2. Det er nå tydelig at begge if sjekkene sjekker om 
// article.imageUrl finnes. Da kan egentlig starte med å sjekke om article.imageUrl er 
// tom og returnere false.

function shouldShowImage(itemIndex, article, showAllImages) {
  if (article.imageUrl == null) {
    return false;
  }
  //...
}

// 3. Siden vi etter denne sjekken vet at article.imageUrl må finnes kan vi da returnere true
// på om showAllImages er true.

function shouldShowImage(itemIndex, article, showAllImages) {
  if (article.imageUrl == null) {
    return false;
  }
  if (showAllImages) {
    return true
  }
  //...
}

// 4. Det utrykket vi står igjen med er da [0, 1, 2].includes(itemIndex) som
// dermed må være enten true eller false.

function shouldShowImage(itemIndex, article, showAllImages) {
  if (article.imageUrl == null) {
    return false;
  }
  if (showAllImages) {
    return true
  }
  return [0,1,2].includes(itemIndex)
}

// 5. Som en siste fix kan vi også gi beskrivende navn til if sjekken og 
// arrayet.

function shouldShowImage(itemIndex, article, showAllImages) {
  const hasImageUrl = article.imageUrl;
  const imageIndexToShow = [0,1,2];

  if (!hasImageUrl) {
    return false
  }
  if (showAllImages) {
    return true
  }
  return imageIndexToShow.includes(itemIndex)
}

// --------------------------
// --------------------------

// Oppgave 4:

interface IUser {
  email: string
};

function c(user: IUser) {
  const userEmail: boolean = user.email.includes('computas');
  if (userEmail) {
    addUser(user);
    alertUser();
  } else {
    alertUser();
  }
}


// Ikke en del av oppgaven
const addUser = (user: IUser) => { }
const alertUser = () => { }

// Issues
// - Duplisert kode (ikke DRY).
// - Forivrrende navngiving: Navnet på userEmail kan oppfattes som den inneholder en streng når det egentlig er en boolean)
// - magical number: Det er ikke tydelig hva computas står for uten å måtte reklektere. Hvis vi tenker oss om kan det beskrive
// domain på epost. F.eks jme@computas.com

// Refactor:
// alertUser() funksjonen blir her kalt i både if og else så denne kan vi egentlig flytte ut.
// Mindre kode gir simplere kode og gjør det lettere å lese.
function validateEmail(user: IUser) {
  const computasDomain = 'computas';
  const isEmailInDomain = user.email.includes(computasDomain);
  if(isEmailInDomain) {
    addUser(user);
  }
  alertUser();
}
// ---------------------------
// ---------------------------

// Oppgave 5:

let fullName = "Ryan McDermott";

function splitIntoFirstAndLastName() {
  fullName = fullName.split(" ");
}

splitIntoFirstAndLastName();

// Issues
// - Unngå sideeffekter: Her utfører splitIntoFirstAndLastName sideeffekter, som vi ikke kan forutse uten
// å måtte lese innholdet på funksjonen. Dette kan lett føre til bugs. Det er klarere å returnere en verdi
// fra funksjonen.

function splitIntoFirstAndLastName(fullName) {
  return fullName.split(" ");
}

const fullName = "Ryan McDermott";
const splitName = splitIntoFirstAndLastName(fullName);

// ---------------------------
// ---------------------------

// Oppgave 6:

function getProduct(htmlResponse) {
  const title: string = htmlResponse.css('.title').html();
  const priceHTML: string = htmlResponse.css('.price').html();
  let price: Number;

  try {
    price = parseInt(priceHTML);
  } catch (e) {
    price = null;
  }

  const sizesHTML: string[] = [htmlResponse.css('.product .size').html()];
  const sizes: string[] = sizesHTML.map((htmlSize) => parseSize(htmlSize));

  return {
    sizes,
    price,
    title
  };
}

// Ikke en del av oppgaven
const parseSize = (htmlSize: string) => htmlSize



// Issues
// - Funkjsonen har for mye ansvar
// - Inkonsistent navngiving (title / priceHTML)

function getProduct(htmlResponse) {
  const title = getTitle(htmlResponse);
  const price = getPrice(htmlResponse);
  const sizes = getProductSizes(htmlResponse);

  return {
    title, 
    price, 
    sizes
  };
}

function getTitle(htmlResponse) {
  return htmlResponse.css('.title').html()
}

function getPrice(htmlResponse) {
  const price = htmlResponse.css('.price').html()
  try {
    return parseInt(price)
  } catch (error) {
    return null; 
  }
}

function getProductSizes(htmlResponse) {
  const sizesHTML: string[] = [htmlResponse.css('.product .size').html()];
  const sizes: string[] = sizesHTML.map((htmlSize) => parseSize(htmlSize));
  return sizes
}

// ---------------------------
// ---------------------------

// Oppgave 7:
// Her har vi en enkel og noe uferdig versjon av MineSweeper. 
// I minesveiper vil en valgt celle enten være tom eller inneholde en mine.
// Hvis man velger en tom celle, vil alle celler rundt som ikke inneholder en
// mine vises. Her har vi kun algoritmen for å vise mineløse celler etter man 
// har klikket på en fri celle.

// Her kan man anta at myTruth og myShow allerede er initialisert med verdier.
// - myTruth vil inneholde 0 i de tilfellene det ikke er noen mine på gitt index. 
// - myShow vil si noe om cellen på gitt index er skjult eller ikke.


class MineSweeper {
  myTruth: Number[][];
  myShow: boolean[][];
  cellPicked(row: number, col: number): void {
    if (this.inBounds(row, col) && !this.myShow[row][col]) {
      this.myShow[row][col] = true;

      if (this.myTruth[row][col] == 0) {
        for (let r = -1; r <= 1; r++) {
          for (let c = -1; c <= 1; c++) {
            cellPicked(row + r, col + c);
          }
        }
      }
    }
  }

  inBounds(row: number, col: number): boolean {
    return 0 <= row && row < this.myTruth.length && 0 <= col && col < this.myTruth[0].length;
  }
}

// Ikke en del av oppgaven
let cellPicked = (i: number, j: number) => { }

// Issues
// - Dårlig navngiving: myShow og myTruth sier ikke så mye om hva de inneholder. cellPicked kan også høres ut som en boolean
// - Komplekse if utrykk: Disse ville være klarer med navn
// - Funksjonen gjør for mye: Det vil være lettere å flytte ut ansvar til flere funksjoner.

// Refactor:
// 1. Vi kan starte med å gi bedre nanv til variablene våre. 
// Siden myTruth skal beksrive om en celle har er mine på eller er fri så beksriver det
// egentlig hele brettet. Board kunne kanskje vært et bedre navn her.

// myShow skal jo beksrive hvilke celler som er vist eller ikke. f.eks cellCheckedState kunne kanskje være
// klarere her.

// 2. Så kan vi gi navn til if utrykkene. if(myTruth[row][col] == 0) sjekker her om cellen er trygg.
// Derfor kan vi kalle den isSafeCell. !myShow[row][col] sjekker her om cellen er valgt fra før.
// Det kan vi heller beskrive med en constant: isCellChecked. Da har vi noe som dette.

// In bounds funksjonen har også flere komplekse sjekker som gjerne kan få nanv.


function cellPicked(row, col) {
    let isCellChecked = cellCheckedState[row][col];
  if (inBounds(row, col) && !isCellChecked) {
    cellCheckedState[row][col] = true;
    const isSafeCell = myTruth[row][col] == 0;

    if (myTruth[row][col] == 0) {
      for (int r = -1; r <= 1; r++) {
        for (int c = -1; c <= 1; c++) {
          cellPicked(row + r, col + c);
        }
      }
    }	
  }
}

function inBounds(row, col) {
  const isRowWithinBounds = 0 <= row && row < myTruth.length;
  const isColumnWithinBounds =  0 <= col && col < myTruth[0].length;
  return isRowWithinBounds && isColumnWithinBounds;
}

// 3. Neste steg kunne vært å flytte ut det rekursive kallet til en egen funksjon. Og 
// gi den et beskrivende navn.

function checkAdjecentCells(row, col) {
  for (let r = -1; r <= 1; r++) {
    for (let c = -1; c <= 1; c++) {
      checkCell(row + r, col + c);
    }
  }
}

// 4. Da sitter vi igjen med noe som dette.

class MineSweeper {
  board: number[][];
	cellCheckedState: boolean[][];
	
	checkCell(row: number, col: number) {
    const isCellChecked = this.cellCheckedState[row][col];
    if (this.inBounds(row, col) && !isCellChecked) {
      this.cellCheckedState[row][col] = true;
      const isSafeCell = this.board[row][col] == 0;

			if (isSafeCell) {
        this.checkAdjecentCells(row, col);
			}
		}
	}

  public checkAdjecentCells(row: number, col: number) {
      for (let r = -1; r <= 1; r++) {
        for (let c = -1; c <= 1; c++) {
          this.checkCell(row + r, col + c);
        }
      }
  }

	inBounds(row: number, col: number) {
    const isRowWithinBounds = 0 <= row && row < this.board.length;
    const isColumnWithinBounds =  0 <= col && col < this.board[0].length;
    return isRowWithinBounds && isColumnWithinBounds;
	}
}

// ---------------------------
// ---------------------------

// Oppgave 8:

// Her har vi en klasse som styrer hvilken mengde discount man kan få basert på gitte typer.
// Vi har her veldig lite informasjon, så prøv så godt som du kan.

// - calculate: Skal regne ut pricen for en gitt kunde.
// - type: Skal her stå for en status om kunden som kan si hvor verdifull kunden er. Vi vet veldig lite her, så vi kan bare anta 4 forskjellige nivåer
// - years: Hvor mange år personen har vært kunde. Kunden får 1% men maksimalt 5 procent i loyalty discount.
// - amount: Den opprinnelige prisen

public class Class1 {
  calculate(amount: number, type: number, years: number) {
    let result = 0;
    const disc = (years > 5) ? 5 / 100 : years / 100; 

    if (type == 1) {
      result = amount;
    } else if (type == 2) {
      result = (amount - (0.1 * amount)) - disc;
    } else if (type == 3) {
      result = (amount - (0.5 * amount)) - disc;
    } else if (type == 4) {
      result = (amount - (0.7 * amount)) - disc;
    }
    return result;
  }
}

// Issues
// - Magic numbers: Her kan vi lage enums of konstanter som beskriver verdiene
// - Dårlig navngiving: Her har vi lite kontekst men kanskje class1 skulle være noe som DiscountManager eller CustomerPriceCalculator
// - Funksjoner gjør for mye
// - Duplicated code: Ikke DRY

enum CustomerStatus {
  Basic,
  Loyal,
  Executive,
  Uber
}

const MAXIMUM_DISCOUNT_FOR_LOYALTY = 5;
const DISCOUNT_FOR_LOYAL_CUSTOMERS = 0.1;
const DISCOUNT_FOR_EXECUTIVE_CUSTOMERS = 0.5;
const DISCOUNT_FOR_UBER_CUSTOMERS = 0.7;

class DiscountManager {
  public getPriceWithDiscount(price: number, customerStatus: CustomerStatus, yearsBeingACustomer: number): number {
    const discountForLoyalty = this.getDiscountForLoyalty(yearsBeingACustomer);
    const discountForCustomerStatus = this.getDiscountForCustomerStatus(price, customerStatus);
    const discountedPrice =  price - discountForCustomerStatus - discountForLoyalty;
    return discountedPrice;
  }

  private getDiscountForLoyalty(yearsBeingACustomer: number) {
    const maxDiscountForLoyalty = MAXIMUM_DISCOUNT_FOR_LOYALTY / 100;

    if (yearsBeingACustomer > MAXIMUM_DISCOUNT_FOR_LOYALTY) {
      return maxDiscountForLoyalty;
    }
    return yearsBeingACustomer / 100;
  }

  private getDiscountForCustomerStatus(price: number, customerStatus: CustomerStatus) {
    switch (customerStatus) {
      case CustomerStatus.Basic:
        return price;
      case CustomerStatus.Loyal:
        return this.getPriceDiscount(DISCOUNT_FOR_LOYAL_CUSTOMERS, price);
      case CustomerStatus.Executive:
        return this.getPriceDiscount(DISCOUNT_FOR_EXECUTIVE_CUSTOMERS, price);
      case CustomerStatus.Uber:
        return this.getPriceDiscount(DISCOUNT_FOR_UBER_CUSTOMERS, price);
    }
  }

  private getPriceDiscount(discount: number, price: number) {
    return discount * price;
  }
}
