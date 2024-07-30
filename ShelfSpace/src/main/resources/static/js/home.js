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

async function fetchStudents() {
    try {
        const response = await fetch('http://localhost:8080/api/Student/getAllStudents');
        if (!response.ok) {
            if (response.status===500) {
                const errorData = await response.json();
                throw new Error(errorData.Error);
            }
            else{
            throw new Error('Network response was not ok');
            }
        }
        const students = await response.json();
        console.log(students); // Debugging: Check fetched data
        displayStudents(students);
    } catch (error) {
        console.error('Error fetching data:', error);
        displayErrorMessage('NO DATA AVAILABLE');
    }
}

function displayStudents(students) {
    const container = document.getElementById('card-container');
    let cardsHTML = '';

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
            cardsHTML += `
<div class="border-2 border-black rounded-lg overflow-hidden bg-black shadow-sm shadow-gray-900 flex flex-col w-36 sm:w-40 md:w-48 lg:w-60">                <div class="relative w-full bg-black p-2">
                    <img src="${book.image}" alt="Book Image" class="rounded-lg w-full h-auto sm:h-auto md:h-auto lg:h-auto object-cover">
                </div>
                <div class="flex flex-col justify-center items-center p-4">
                    <div class="text-sm sm:text-sm md:text-md lg:text-lg font-bold mb-2 text-center text-white">${book.bookName}</div>
                    <div class="text-white text-sm text-center sm:text-sm md:text-md lg:text-lg">
                        <div class="font-semibold mb-1 text-white">${student.name}</div>
                        <div>${book.returnDate}</div>
                    </div>
                </div>
                 <div class="flex flex-col justify-center items-center p-1 -mt-3">
                <a href="#" class="text-red-500 ">View More</a>
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
    container.classList.remove('grid', 'grid-cols-2', 'grid-rows-6', 'gap-4', 'sm:grid-cols-2', 'sm:grid-rows-6', 'md:grid-cols-3', 'md:grid-rows-4', 'lg:grid-cols-4', 'lg:grid-rows-3');
    container.classList.add('flex', 'items-center', 'justify-center' , 'w-full' , '-mt-8');
    container.innerHTML = `
        <div class="flex flex-col items-center justify-center w-full h-auto">
            <h1 class = "text-white self-center text-base sm:text-base md:text-md lg:text-lg font-semibold whitespace-nowrap" >${message}</h1>
        </div>
    `;
}