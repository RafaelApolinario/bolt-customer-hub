param(
    [string]$BaseUrl = "http://localhost:8082"
)

$ErrorActionPreference = "Stop"

function Invoke-JsonRequest {
    param(
        [string]$Method,
        [string]$Uri,
        [object]$Body = $null
    )

    $params = @{
        Method = $Method
        Uri = $Uri
        UseBasicParsing = $true
    }

    if ($null -ne $Body) {
        $params.ContentType = "application/json"
        $params.Body = ($Body | ConvertTo-Json -Depth 10)
    }

    try {
        return Invoke-WebRequest @params
    } catch {
        if ($_.Exception.Response) {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $errorBody = $reader.ReadToEnd()
            Write-Host $errorBody
        }
        throw
    }
}

function Assert-Status {
    param(
        [int]$Actual,
        [int]$Expected,
        [string]$Step
    )

    if ($Actual -ne $Expected) {
        throw "$Step failed. Expected HTTP $Expected, got HTTP $Actual."
    }
}

$suffix = Get-Date -Format "MMddHHmmss"
$document = "99$suffix"
$unitNumber = "UC-SMOKE-$suffix"
$secondUnitNumber = "UC-SMOKE-$suffix-2"

Write-Host "Checking health..."
$health = Invoke-JsonRequest -Method Get -Uri "$BaseUrl/api/health"
Assert-Status -Actual $health.StatusCode -Expected 200 -Step "Health check"

Write-Host "Creating customer..."
$createBody = @{
    name = "Smoke Test Customer"
    document = $document
    consumerUnits = @(
        @{
            number = $unitNumber
            zipCode = "30140071"
        }
    )
}
$createdResponse = Invoke-JsonRequest -Method Post -Uri "$BaseUrl/api/customers" -Body $createBody
Assert-Status -Actual $createdResponse.StatusCode -Expected 201 -Step "Create customer"
$created = $createdResponse.Content | ConvertFrom-Json
$customerId = $created.id

Write-Host "Listing customers..."
$listResponse = Invoke-JsonRequest -Method Get -Uri "$BaseUrl/api/customers"
Assert-Status -Actual $listResponse.StatusCode -Expected 200 -Step "List customers"

Write-Host "Fetching customer by id..."
$getResponse = Invoke-JsonRequest -Method Get -Uri "$BaseUrl/api/customers/$customerId"
Assert-Status -Actual $getResponse.StatusCode -Expected 200 -Step "Get customer by id"

Write-Host "Updating customer..."
$updateBody = @{
    name = "Smoke Test Customer Updated"
    document = $document
    consumerUnits = @(
        @{
            number = $unitNumber
            zipCode = "30140071"
        },
        @{
            number = $secondUnitNumber
            zipCode = "30140071"
        }
    )
}
$updateResponse = Invoke-JsonRequest -Method Put -Uri "$BaseUrl/api/customers/$customerId" -Body $updateBody
Assert-Status -Actual $updateResponse.StatusCode -Expected 200 -Step "Update customer"

Write-Host "Listing latest customers..."
$latestResponse = Invoke-JsonRequest -Method Get -Uri "$BaseUrl/api/customers/latest"
Assert-Status -Actual $latestResponse.StatusCode -Expected 200 -Step "List latest customers"

Write-Host "Deleting customer logically..."
$deleteResponse = Invoke-JsonRequest -Method Delete -Uri "$BaseUrl/api/customers/$customerId"
Assert-Status -Actual $deleteResponse.StatusCode -Expected 204 -Step "Delete customer"

Write-Host "API smoke test completed successfully."
