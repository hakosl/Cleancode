// Oppgavene under (skrevet i typescript) bryter med en eller flere Clean Code prinsipper.
// Se hvor mange du klarer å identifisere. Prøv gjerne også å se om
// du klarer refaktorere dem.

// ---------------------------
// ---------------------------


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

// ---------------------------
// ---------------------------

// Oppgave 2:

function canBuyBeer(age: number, money: number) {
  if (age >= 21 && money >= 20) {
    return true
  }
  return false
}

// --------------------------
// --------------------------

// Oppgave 3:

// Her er det brukt javascript ternaries som basically er det samme som if else.
// if = utrykk  
// ? = utrykk som evalures hvis true
// : = utrykk som evalueres hvis false

function shouldShowImage(itemIndex: number, article: {imageUrl: string}, showAllImages: boolean) {
  return [0, 1, 2].includes(itemIndex)
    ? article.imageUrl != null
    : showAllImages
      ? article.imageUrl != null
      : false
}

// --------------------------
// --------------------------

interface IUser {
  email: string
};

// Oppgave 4:
function f(user: IUser) {
  const userEmail: boolean = user.email.includes('computas');
  if (userEmail) {
    addUser(user);
    alertUser();
  } else {
    alertUser();
  }
}

// ---------------------------
// ---------------------------

// Oppgave 5:

let n: any = "Ryan McDermott";

function splitIntoFirstAndLastName() {
  n = n.split(" ");
}
splitIntoFirstAndLastName();

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

// ---------------------------
// ---------------------------

// ---------------------------
// Ekstraoppgaver
// ---------------------------

// Oppgave 7:

// Her har vi en enkel og noe uferdig versjon av MineSweeper. 

// Her kan man anta at myTruth og myShow allerede er initialisert med verdier.
// - MyTruth vil inneholde 0 i de tilfellene det ikke er noen mine på gitt index. 
// - MyShow vil si noe om cellen på gitt index er skjult eller ikke.



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

// ---------------------------
// ---------------------------

// Oppgave 8:

// Her har vi en klasse som styrer hvilken mengde discount man kan få basert på gitte typer.
// Vi har her veldig lite informasjon, så prøv så godt som du kan.

// - calculate: Skal regne ut pricen for en gitt kunde.
// - type: Skal her stå for en status om kunden som kan si hvor verdifull kunden er og dermed bestemmer hvilken discount de får. Vi vet veldig lite her, så vi kan bare anta 4 forskjellige nivåer
// - years: Hvor mange år personen har vært kunde. Kunden får 1% men maksimalt 5 procent i loyalty discount.
// - amount: Den opprinnelige prisen
// - result: Dette er så den totale prisen med loyaltydiscount og kundediscount trukket fra

function calculate(amount: number, type: number, years: number): number {
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
