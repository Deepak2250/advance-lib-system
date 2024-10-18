// Get the button and navbar elements
const toggleButton = document.querySelector('[data-collapse-toggle]');
const navbar = document.getElementById('navbar-cta');

// Event listener for button click
toggleButton.addEventListener('click', function() {
    // Check if navbar is already open
    const isOpen = !navbar.classList.contains('-translate-x-full');
    
    // Toggle the navbar visibility
    navbar.classList.toggle('translate-x-0', !isOpen);
    navbar.classList.toggle('-translate-x-full', isOpen);
    
    // Toggle scrolling on the body
    document.body.style.overflow = isOpen ? '' : 'hidden';
});

// Cards 

let pageNumber = 1;
const pageSize = 12;

async function fetchStudents() {

    try {
        const response = await fetch(`http://localhost:8080/api/Student/getAllStudents?pageNumber=${pageNumber}&size=${pageSize}`);
        if (!response.ok) {
            if (response.status===500) {
                const errorData = await response.json();
                displayNetworkIssueReloadButton();
                throw new Error(errorData.Error);
            }
            else{
            throw new Error('Network response was not ok');
            }
        }
        const studentResponse = await response.json();
        const student = studentResponse.studentDetails;
        const dataLength = student.length;
        const nextButtonVar = document.getElementById("nextButton");
        const previousButton = document.getElementById("previousButton");
        console.log(student);
        
        
        let container = document.getElementById('card-container');
        if (dataLength === 1) {
            container.className = "mt-2 grid grid-cols-2 grid-rows-1 sm:grid-cols-1 md:grid-cols-1 lg:grid-cols-1 sm:grid-rows-1 md:grid-rows-1 lg:grid-rows-1";
        }

        else if(dataLength <= 2){
            container.className = "mt-2 grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-4 sm:grid-rows-1 md:grid-rows-1 lg:grid-rows-1";
        }

        else if (dataLength > 2 && dataLength <= 4) {
            container.className = "mt-2 grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-2 md:grid-rows-2 lg:grid-rows-1 ";
        }
        else if (dataLength > 4 && dataLength<=8) {
            container.className = "mt-2 grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-4 md:grid-rows-3 lg:grid-rows-2 ";
        }
        else if (dataLength > 8 && dataLength<=12) {
            container.className = "mt-2 grid grid-cols-2 grid-rows-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 sm:grid-rows-6 md:grid-rows-4 lg:grid-rows-3 ";
        }
        
        displayStudents(student);

        if (studentResponse.hasMore) {
            nextButtonVar.style.color = "white"; // Reset to default color
            nextButtonVar.disabled = false;
        } else {
            nextButtonVar.style.color = "gray";
            nextButtonVar.disabled = true;
        }

        if (pageNumber === 1) {
            previousButton.style.color = "gray";
            previousButton.disabled = true;
        } else {
            previousButton.style.color = "white"; // Reset to default color
            previousButton.disabled = false;
        }


          nextButtonVar.onclick = async () => {
                pageNumber += 1; // Increment page number
                await fetchStudents(); // Fetch the next page of students
            };

            previousButton.onclick = async() => {
                pageNumber -= 1;
                await fetchStudents();
            }

    } catch (error) {
        console.error('Error fetching data:', error);
        displayErrorMessage('NO DATA AVAILABLE');
    }
}

fetchStudents();

function displayStudents(students) {
    const container = document.getElementById('card-container');
    let cardsHTML = '';
    const image = "/images/No image.jpg"; // Adjusted path
    console.log(students.length);
    

    if (!students || students.length === 0) {
        container.innerHTML = `
            <div class="flex items-center justify-center w-full h-auto">
                <h1>Not Available</h1>
            </div>
        `;
        return;
    }

    students.forEach(student => {
        student.booksDetails.forEach(book => {
            const studentBookImage = book.image ? book.image : image;
            cardsHTML += `
            <div class="flex justify-center">
<div class="border-2 border-black rounded-lg overflow-hidden bg-black shadow-sm shadow-gray-900 flex flex-col w-36 sm:w-40 md:w-48 lg:w-60">                <div class="relative w-full bg-black p-2">
                    <img src="${studentBookImage}" alt="Book Image" class="rounded-lg w-full h-auto sm:h-auto md:h-auto lg:h-auto object-cover">
                </div>
                <div class="flex flex-col justify-center items-center p-4">
                    <div class="text-sm sm:text-sm md:text-md lg:text-lg font-bold mb-2 text-center text-white">${book.bookName}</div>
                    <div class="text-white text-sm text-center sm:text-sm md:text-md lg:text-lg">
                        <div class="font-semibold mb-1 text-white">${student.name}</div>
                        <div>${book.returnDate}</div>
                    </div>
                </div>
                 <div class="flex flex-col justify-center items-center p-1 -mt-3">
                <a href="/viewmore?roll_no=${student.roll_no}" class="text-red-500 ">View More</a>
            </div>
            </div>
            </div>
            `;
        });
    });

    container.innerHTML = cardsHTML;
}

document.addEventListener('DOMContentLoaded', fetchStudents);

function displayErrorMessage(message) {
    const container = document.getElementById('card-container');
    container.classList.remove('grid', 'gap-4');
    container.classList.add('flex', 'items-center', 'justify-center' , 'w-full' , '-mt-8');
    container.innerHTML = `
        <div class="flex flex-col items-center justify-center w-full h-auto">
            <h1 class = "text-white self-center text-base sm:text-base md:text-md lg:text-lg font-semibold whitespace-nowrap" >${message}</h1>
        </div>
    `;

    const nextPageContainer = document.getElementById("nextPageContainer");
    document.removeChild(nextPageContainer);
}

function displayNetworkIssueReloadButton(){
    window.onload();
    let container = document.getElementById('card-container');
    container.classList.remove('grid', 'grid-cols-2', 'grid-rows-6', 'gap-4', 'sm:grid-cols-2', 'sm:grid-rows-6', 'md:grid-cols-3', 'md:grid-rows-4', 'lg:grid-cols-4', 'lg:grid-rows-3');
    container.classList.add('flex', 'items-center', 'justify-center' , 'w-full' , '-mt-8');
    container.innerHTML = `
        <div class="flex flex-col items-center justify-center w-full h-auto">
            <button class = "text-black bg-white self-center text-base sm:text-base md:text-md lg:text-lg font-semibold whitespace-nowrap" >Reload The Page </h1>
        </div>
    `;
}
