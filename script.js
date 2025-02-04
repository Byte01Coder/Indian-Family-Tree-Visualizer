document.getElementById("fileInput").addEventListener("change", handleFile);

function handleFile(event) {
    const file = event.target.files[0];
    if (file) {
        const fileName = file.name.toLowerCase();
        if (!fileName.endsWith(".csv")) {
            alert("Please upload a valid CSV file.");
            return;
        }
        Papa.parse(file, {
            complete: function (results) {
                const data = results.data;
                if (data.length === 0) {
                    alert("The CSV file is empty. Please upload a valid file.");
                    return;
                }
                const familyTree = buildTree(data);
                renderTree(familyTree);
            },
            header: true,
            skipEmptyLines: true,
        });
    }
}

function buildTree(data) {
    const nodes = {};
    const uniqueRelationships = new Set();

    data.forEach(row => {
        const name = row.name?.trim();
        if (name) {
            if (!nodes[name]) nodes[name] = { name, parents: [], children: [], spouses: [] };
        }
    });

    data.forEach(row => {
        const name = row.name?.trim();
        const parents = row.parents ? row.parents.split(';').map(p => p.trim()) : [];
        const spouses = row.spouses ? row.spouses.split(';').map(s => s.trim()) : [];
        const node = nodes[name];

        if (!node) return;

        parents.forEach(parentName => {
            if (parentName && nodes[parentName]) {
                node.parents.push(nodes[parentName]);
                nodes[parentName].children.push(node);
            }
        });

        spouses.forEach(spouseName => {
            if (spouseName && nodes[spouseName] && !node.spouses.includes(nodes[spouseName])) {
                node.spouses.push(nodes[spouseName]);
            }
        });
    });

    Object.values(nodes).forEach(node => {
        const relationshipKey = JSON.stringify({ parents: node.parents.map(p => p.name), children: node.children.map(c => c.name) });
        if (uniqueRelationships.has(relationshipKey)) {
            delete nodes[node.name];
        } else {
            uniqueRelationships.add(relationshipKey);
        }
    });

    return Object.values(nodes).filter(node => node.parents.length === 0);
}

function renderTree(roots) {
    const container = document.getElementById("tree-container");
    container.innerHTML = "";
    if (roots.length === 0) {
        container.innerHTML = "<p>No data to display. Please upload a valid CSV file.</p>";
        return;
    }
    
    const treeHTML = roots.map(root => {
        return generateTreeHTML(root, new Set(), new Set());
    }).join("");
    
    container.innerHTML = `<div class="tree">${treeHTML}</div>`;
}

function generateTreeHTML(node, visited, processedSpouses) {
    if (!node || visited.has(node.name)) return "";
    visited.add(node.name);

    const childrenHTML = node.children.map(child => generateTreeHTML(child, visited, processedSpouses)).join("");
    const spousesHTML = node.spouses
        .filter(spouse => !processedSpouses.has(`${node.name}-${spouse.name}`) && !processedSpouses.has(`${spouse.name}-${node.name}`))
        .map(spouse => {
            processedSpouses.add(`${node.name}-${spouse.name}`);
            processedSpouses.add(`${spouse.name}-${node.name}`);
            return `<div class="spouse">${spouse.name}</div>`;
        })
        .join("");

    const randomColor = getRandomColor();

    return `
        <div class="family" style="border: 2px solid ${randomColor}; padding: 10px; margin: 10px; border-radius: 8px;">
            <div class="node" data-name="${node.name}" style="margin-left: 20px;">
                <div class="person">
                    <div class="name">${node.name}</div>
                    <div class="spouses">${spousesHTML}</div>
                </div>
                ${childrenHTML ? `<div class="children">${childrenHTML}</div>` : ""}
            </div>
        </div>
    `;
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
